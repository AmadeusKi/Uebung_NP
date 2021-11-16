import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private HashMap<Sensor, Double> sensorMessWerte = new HashMap<>();

    private Label valFL = new Label("");
    private Label valFR = new Label("");
    private Label valBL = new Label("");
    private Label valBR = new Label("");

    private Label lFL = new Label("Sensor FL");
    private Label lFR = new Label("Sensor FR");
    private Label lBL = new Label("Sensor HL");
    private Label lBR = new Label("Sensor HR");
    private Label lSpeed = new Label("");
    private Label lSteering = new Label("");

    private Slider sliderFL = new Slider(0, 100, 50);
    private Slider sliderFR = new Slider(0, 100, 50);
    private Slider sliderBL = new Slider(0, 100, 50);
    private Slider sliderBR = new Slider(0, 100, 50);

    Button sendValues = new Button("Messwerte abschicken");

    private Controller c = new Controller(this, this);

    private void addSliderListener(Slider slider, Sensor s){
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sensorMessWerte.put(s,newValue.doubleValue());
            }
        });
    }

    private void addLabelListener(Slider slider, Label l){
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                l.setText(newValue.toString());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent(){

        HBox hboxLeft = new HBox(10);

        VBox vboxLeft = new VBox(10); // spacing = 8
        VBox vboxRight = new VBox(10);

        vboxLeft.setMinWidth(300);
        vboxRight.setMinWidth(300);

        initSlider();
        initHashmap();
        initButton();

        vboxLeft.getChildren().addAll(lFL,sliderFL,valFL, lBL, sliderBL, valBL, sendValues);

        vboxRight.getChildren().addAll(lFR, sliderFR, valFR, lBR, sliderBR, valBR, lSpeed, lSteering);

        hboxLeft.getChildren().addAll(vboxLeft, vboxRight);

        return hboxLeft;
    }

    private void initButton() {
        sendValues.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    c.control(sensorMessWerte);
                    lSpeed.setText("current Speed: " + String.valueOf(currentSpeed));
                    lSteering.setText("current Steering: " + String.valueOf(currentSteering));
                } catch (CarException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initHashmap() {
        sensorMessWerte.put(CarSensorInput.Sensor.FL, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.FR, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.BL, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.BR, 50d);
    }

    private void initSlider() {

        sliderFL.setShowTickLabels(true);
        sliderFL.setShowTickMarks(true);
        addLabelListener(sliderFL, valFL);
        addSliderListener(sliderFL, Sensor.FL);


        sliderFR.setShowTickLabels(true);
        sliderFR.setShowTickMarks(true);
        addLabelListener(sliderFR, valFR);
        addSliderListener(sliderFR, Sensor.FR);


        sliderBL.setShowTickLabels(true);
        sliderBL.setShowTickMarks(true);
        addLabelListener(sliderBL, valBL);
        addSliderListener(sliderBL, Sensor.BL);


        sliderBR.setShowTickLabels(true);
        sliderBR.setShowTickMarks(true);
        addLabelListener(sliderBR, valBR);
        addSliderListener(sliderBR, Sensor.BR);
    }

    @Override
    public void start(Stage primaryStage) throws CarException {

        primaryStage.setTitle("GUI");

        primaryStage.setScene(new Scene(createContent(), 700,700));
        startSensors();
        primaryStage.show();
    }

    private void startSensors() throws CarException {
        c.chkSensorFL();
        c.chkSensorFR();
        c.chkSensorBL();
        c.chkSensorBR();

    }

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
}
