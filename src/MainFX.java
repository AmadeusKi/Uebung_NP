import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GUI cs = new GUI();


        Controller c = new Controller(cs, cs);
        cs.setController(c);

        cs.initHashmap();      // Befüllen der Hashmap mit Initialwerten
        cs.startSensors();     // Starten der SensorThreads
        cs.initControlThread();


        primaryStage.setTitle("GUI Input-Output-Simulation");
        primaryStage.setScene(new Scene(cs.createContent(), 700,700));
        primaryStage.show();
    }
}
