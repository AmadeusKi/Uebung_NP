import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class GUI extends Application implements CarSensorInput, CarMotorOutput{

    private int currentSpeed;
    private int currentSteering;

    private HashMap<Sensor, Double> sensorMessWerte;

    private Label valFL;
    private Label valFR;
    private Label valBL;
    private Label valBR;

    private Label lSpeed;
    private Label lSteering;

    private Label lFL;
    private Label lFR;
    private Label lBL;
    private Label lBR;

    private Slider sliderFL;
    private Slider sliderFR;
    private Slider sliderBL;
    private Slider sliderBR;

    private Controller c = new Controller(this, this);

    //Listener, welcher bei Änderung der Slider die Sensorwerte aktualisiert und in die GUI einträgt
    private void addListener(Slider slider, Sensor s, Label l){
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sensorMessWerte.put(s,newValue.doubleValue());
                l.setText(newValue.toString());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    //Erstellung des Layout's und Erzeugung der GUI-Elemente
    private Parent createContent(){

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10,10,10,10));
        VBox vboxLeft = new VBox(10);
        VBox vboxRight = new VBox(10);

        vboxLeft.setMinWidth(300);
        vboxRight.setMinWidth(300);

        initLabel();
        initSlider();

        vboxLeft.getChildren().addAll(lFL,sliderFL,valFL, lBL, sliderBL, valBL);
        vboxRight.getChildren().addAll(lFR, sliderFR, valFR, lBR, sliderBR, valBR, lSpeed, lSteering);
        hBox.getChildren().addAll(vboxLeft, vboxRight);

        return hBox;
    }

    // Methode zur Initialisierung der Label
    private void initLabel() {
        lBR = new Label("Sensor BR");
        lBL = new Label("Sensor BL");
        lFR = new Label("Sensor FR");
        lFL = new Label("Sensor FL");

        valFL = new Label("");
        valFR = new Label("");
        valBL = new Label("");
        valBR = new Label("");

        lSpeed = new Label("");
        lSteering = new Label("");

    }

    // Methode, welche die Hashmap initialisiert und mit default-Werten bestückt
    // Die default-Werte der Hashmap entsprechen den Slider-default-Werten
    private void initHashmap() {
        sensorMessWerte = new HashMap<>(4);
        sensorMessWerte.put(CarSensorInput.Sensor.FL, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.FR, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.BL, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.BR, 50d);
    }

    //Methode zum Initialisieren der Slider
    private void initSlider() {

        sliderFL = new Slider(0, 100, 50);
        sliderFL.setShowTickLabels(true);
        sliderFL.setShowTickMarks(true);
        addListener(sliderFL,Sensor.FL, valFL);

        sliderFR = new Slider(0, 100, 50);
        sliderFR.setShowTickLabels(true);
        sliderFR.setShowTickMarks(true);
        addListener(sliderFR,Sensor.FR, valFR);

        sliderBL = new Slider(0, 100, 50);
        sliderBL.setShowTickLabels(true);
        sliderBL.setShowTickMarks(true);
        addListener(sliderBL, Sensor.BL, valBL);

        sliderBR = new Slider(0, 100, 50);
        sliderBR.setShowTickLabels(true);
        sliderBR.setShowTickMarks(true);
        addListener(sliderBR,Sensor.BR, valBR);
    }

    @Override
    public void start(Stage primaryStage) throws CarException {

        initHashmap();      // Befüllen der Hashmap mit Initialwerten
        startSensors();     // Starten der SensorThreads
        controlThread t = new controlThread();
        t.start();      // Starten des Threads, welcher den Motor steuert.

        primaryStage.setTitle("GUI Input-Output-Simulation");
        primaryStage.setScene(new Scene(createContent(), 700,700));
        primaryStage.show();
    }

    //Methode zum Starten der Sensorthreads
    private void startSensors() throws CarException {
        c.chkSensorFL();
        c.chkSensorFR();
        c.chkSensorBL();
        c.chkSensorBR();
    }

    // Methoden, welche von den Schnittstellen vererbt wurden.
    @Override
    public double getDistance(Sensor s) throws CarException {
        return sensorMessWerte.get(s);
    }

    @Override
    public void setSpeed(int x) throws CarException {
        currentSpeed = x;
    }

    @Override
    public void steering(int x) throws CarException {
        currentSteering = x;
    }

    // Klasse welche den Steuerthread beinhaltet
    // lässt Sensorwerte überprüfen und passt dementsprechend das GUI an
    class controlThread extends Thread{

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
