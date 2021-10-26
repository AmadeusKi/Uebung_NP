import java.util.HashMap;

public class Test1 implements CarSensorInput, CarMotorOutput{

    private int testSpeed = 999;
    int testSteering = 999;

    @Override
    public void setSpeed(int x) throws CarException {
        testSpeed = x;
    }

    @Override
    public void steering(int x) throws CarException {
        testSteering = x;
    }

    @Override
    public double getDistance(Sensor s) throws CarException {
//        messWert = Math.random()*100;
//        return messWert;
        return 8;
    }

    public static void main(String[] args) throws CarException {
        Test1 test = new Test1();
        CarSensorInput testSensor = new Test1();
        CarMotorOutput testMotor = new Test1();

        Controller testController = new Controller(testSensor, testMotor);

        //Test von getDistance
        Test1.annahmeGleichDouble(testController.cs.getDistance(Sensor.FL), 8);

        //Test von speed und steering
        HashMap<Sensor, Double> testSensorMessWerte = new HashMap<>();
        testSensorMessWerte.put(Sensor.FL, 5d);
        testSensorMessWerte.put(Sensor.FR, 5d);
        testSensorMessWerte.put(Sensor.BL, 5d);
        testSensorMessWerte.put(Sensor.BR, 5d);

        annahmeGleichInt(test.testSpeed, 999);
        testController.control(testSensorMessWerte);
        annahmeGleichInt(test.testSpeed, 0);




    }

    public static void annahmeGleichDouble(double ist, double soll){
        if (ist == soll){
            System.out.println("Test erfolgreich");
        }
        else {
            System.out.println("Test gescheitert");
            System.out.println("erwarteter Wert: " + soll);
            System.out.println("erhaltener Wert: " + ist);
        }
    }

    public static void annahmeGleichInt(int ist, int soll){
        if (ist == soll){
            System.out.println("Test erfolgreich");
        }
        else {
            System.out.println("Test gescheitert");
            System.out.println("erwarteter Wert: " + soll);
            System.out.println("erhaltener Wert: " + ist);
        }
    }
}
