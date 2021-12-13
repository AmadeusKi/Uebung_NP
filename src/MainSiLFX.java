import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainSiLFX extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception {
        SilTest st = new SilTest();
        GUI gui = new GUI();
        Controller c = new Controller(st, gui);
        gui.setController(c);
        gui.initControlThread();
        c.startThreads();

        primaryStage.setTitle("GUI Input-Output-Simulation");
        primaryStage.setScene(new Scene(gui.createContent(), 700,700));
        primaryStage.show();

        //starten der Test-Sequenzen aus der SiLTest-Klasse
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    st.startTest(c);
                } catch (CarException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }



}
