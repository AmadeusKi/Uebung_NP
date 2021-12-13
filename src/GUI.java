import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;

import java.util.HashMap;

public class GUI implements CarSensorInput, CarMotorOutput{

    private int currentSpeed;
    private int currentSteering;

    private HashMap<Sensor, Double> sensorMessWerte;

    // Label für Momentanwerte der Slider
    private Label valV;
    private Label valH;
    private Label valL;
    private Label valR;

    // Label für momentane Geschw. und Lenkung
    private Label lSpeed;
    private Label lSteering;

    // Label zur Beschriftung der Sensorslider
    private Label lV;
    private Label lH;
    private Label lL;
    private Label lR;

    // Slider zum einstellen der Sensorwerte
    private Slider sliderV;
    private Slider sliderH;
    private Slider sliderL;
    private Slider sliderR;

    private Controller c = new Controller(this, this);

    public void setController(Controller c){
        this.c = c;
    }

    public GUI() throws CarException {
    }

    //Listener, welcher bei Änderung der Slider die Sensorwerte aktualisiert und in die GUI einträgt
    public void addListener(Slider slider, Sensor s, Label l){
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sensorMessWerte.put(s,newValue.doubleValue());
                l.setText(newValue.toString());
            }
        });
    }



    //Erstellung des Layout's und Erzeugung der GUI-Elemente
    public Parent createContent(){

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10,10,10,10));
        VBox vboxLeft = new VBox(10);
        VBox vboxRight = new VBox(10);

        vboxLeft.setMinWidth(300);
        vboxRight.setMinWidth(300);

        initLabel();
        initSlider();

        vboxLeft.getChildren().addAll(lV, sliderV, valV, lL, sliderL, valL);
        vboxRight.getChildren().addAll(lH, sliderH, valH, lR, sliderR, valR, lSpeed, lSteering);
        hBox.getChildren().addAll(vboxLeft, vboxRight);

        return hBox;
    }

    // Methode zur Initialisierung der Label
    public void initLabel() {
        lR = new Label("Sensor BR");
        lL = new Label("Sensor BL");
        lH = new Label("Sensor H");
        lV = new Label("Sensor V");

        valV = new Label("");
        valH = new Label("");
        valL = new Label("");
        valR = new Label("");

        lSpeed = new Label("");
        lSteering = new Label("");

    }

    // Methode, welche die Hashmap initialisiert und mit default-Werten bestückt
    // Die default-Werte der Hashmap entsprechen den Slider-default-Werten
    public void initHashmap() {
        sensorMessWerte = new HashMap<>(4);
        sensorMessWerte.put(CarSensorInput.Sensor.V, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.H, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.R, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.L, 50d);


    }

    public void initControlThread(){
        GUI.ControlThread t = new GUI.ControlThread();
        t.start();      // Starten des Threads, welcher den Motor steuert.
    }

    //Methode zum Initialisieren der Slider
    // min-Wert 0 cm, Max-Wert 100 cm, default-Wert 50 cm
    // den Slidern werden Listener hinzugefügt
    public void initSlider() {

        sliderV = new Slider(0, 100, 50);
        sliderV.setShowTickLabels(true);
        sliderV.setShowTickMarks(true);
        addListener(sliderV,Sensor.V, valV);

        sliderH = new Slider(0, 100, 50);
        sliderH.setShowTickLabels(true);
        sliderH.setShowTickMarks(true);
        addListener(sliderH,Sensor.H, valH);

        sliderL = new Slider(0, 100, 50);
        sliderL.setShowTickLabels(true);
        sliderL.setShowTickMarks(true);
        addListener(sliderL, Sensor.L, valL);

        sliderR = new Slider(0, 100, 50);
        sliderR.setShowTickLabels(true);
        sliderR.setShowTickMarks(true);
        addListener(sliderR,Sensor.R, valR);
    }

    //Methode zum Starten der Sensorthreads
    public void startSensors() throws CarException {
        c.startThreads();
    }

    // Methoden, welche von den Schnittstellen vererbt wurden.
    @Override
    public double getDistance(Sensor s) throws CarException {
        return sensorMessWerte.get(s);
    }

    // x entspricht der Prozentzahl der Maximalgeschwindigkeit (-100%-0-100%; rückwärts-stop-vorwärts)
    public void setSpeed(int x) throws CarException {
        currentSpeed = x;
    }

    // x entpricht der Prozentzahl des maximalen Einschlag (rechts -> 100 %; links -100 %
    public void steering(int x) throws CarException {
        currentSteering = x;
    }

    // Klasse welche den Steuerthread beinhaltet
    // lässt Sensorwerte überprüfen und passt dementsprechend das GUI an
    public class ControlThread extends Thread{

        public void run(){

            while (true){
                try {
                    c.control(c.getSensorMessWerte());
                } catch (CarException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    lSpeed.setText("current Speed: " + currentSpeed + "%");
                    lSteering.setText("current Steering: " + currentSteering + "%");
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
