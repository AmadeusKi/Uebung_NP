import java.util.HashMap;

public class Test1 implements CarSensorInput, CarMotorOutput{

    private int testSpeed = 999;
    private int testSteering = 999;
    private double testDistance = 8;
    private Controller testController;
    private HashMap<Sensor, Double> sensorMessWerte;

    Test1(){
        sensorMessWerte = new HashMap<>(4);
        sensorMessWerte.put(Sensor.V, 100d);
        sensorMessWerte.put(Sensor.H, 100d);
        sensorMessWerte.put(Sensor.R, 100d);
        sensorMessWerte.put(Sensor.L, 100d);
    }
    @Override
    public void setSpeed(int x) throws CarException {
        this.testSpeed = x;
    }

    @Override
    public void steering(int x) throws CarException {
        this.testSteering = x;
    }

    @Override
    public double getDistance(Sensor s) throws CarException {
//        messWert = Math.random()*100;
//        return messWert;
        return this.testDistance;
    }

    public static void main(String[] args) throws CarException, InterruptedException {

        Test1 test1 = new Test1();
        test1.controllerTest();
        test1 = new Test1();
        test1.testGetDistance(test1);




    }

    private void controllerTest() throws CarException, InterruptedException {
        testController = new Controller(this, this);
        testController.startThreads();
        Thread.sleep(2000);

        System.out.println("Test Messwert BR erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.L),8d));
        System.out.println("Test Messwert BL erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.R),8d));
        System.out.println("Test Messwert FR erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.H),8d));
        System.out.println("Test Messwert FL erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.V),8d));

        System.out.println("Test Control (Bremsung) erfolgreich? " + annahmeGleichInt(testSpeed, 0));

        //Test "genug Platz um das Auto"
        testController.getSensorMessWerte().put(Sensor.R, 30d);
        testController.getSensorMessWerte().put(Sensor.L, 30d);
        testController.getSensorMessWerte().put(Sensor.V, 30d);
        testController.getSensorMessWerte().put(Sensor.H, 30d);

        testController.control(testController.getSensorMessWerte());

        System.out.println("Test Control (Gas geben) erfolgreich? " + annahmeGleichInt(testSpeed, 100));

        //
        testController.getSensorMessWerte().put(Sensor.R, 5d);
        testController.getSensorMessWerte().put(Sensor.L, 30d);
        testController.getSensorMessWerte().put(Sensor.V, 5d);
        testController.getSensorMessWerte().put(Sensor.H, 30d);

        testController.control(testController.getSensorMessWerte());

        System.out.println("Test Lenkung erfolgreich? " + annahmeGleichInt(testSteering, 100));

    }

    private void testGetDistance(CarSensorInput s) throws CarException {
        System.out.println("Test getDistance() erfolgreich?  " + annahmeGleichDouble(s.getDistance(Sensor.V),8d));

    }

    public static boolean annahmeGleichDouble(double ist, double soll){
        if (ist == soll){
            return true;
            }
        else {
            System.out.println("Der Wert beträgt: " + ist);
            return false;
        }
    }

    public static boolean annahmeGleichInt(int ist, int soll){
        if (ist == soll){
            return true;
        }
        else {
            System.out.println("Der Wert beträgt: " + ist);
            return false;
        }
    }
}
