import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUIInput extends Application implements CarSensorInput{

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createWindow(){
        return new StackPane(new Text("Hallo World"));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createWindow(), 500,500));
        primaryStage.show();
    }

    @Override
    public double getDistance(Sensor s) throws CarException {
        return 0;
    }
}
