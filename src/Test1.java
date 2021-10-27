public class Test1 implements CarSensorInput, CarMotorOutput{

    private int testSpeed = 999;
    int testSteering = 999;
    double testDistance = 8;

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
        return testDistance;
    }

    public static void main(String[] args) throws CarException, InterruptedException {

        CarSensorInput testSensor = new Test1();
        CarMotorOutput testMotor = new Test1();

        Controller testController = new Controller(testSensor, testMotor);

        testGetDistance(testSensor);
        controllerTest(testController);

    }




    private static void controllerTest(Controller testController) throws CarException, InterruptedException {
        testController.chkSensorBL();
        testController.chkSensorBR();
        testController.chkSensorFL();
        testController.chkSensorFR();
        Thread.sleep(2000);

        System.out.println("Test Messwert BR erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.BR),8d));
        System.out.println("Test Messwert BL erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.BL),8d));
        System.out.println("Test Messwert FR erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.FR),8d));
        System.out.println("Test Messwert FL erfolgreich? " + annahmeGleichDouble(testController.getSensorMessWerte().get(Sensor.FL),8d));

        System.out.println("Test Control erfolgreich? " + annahmeGleichInt(testController.getCurrentSpeed(), 0));




    }

    private static void testGetDistance(CarSensorInput s) throws CarException {
        System.out.println("Test getDistance() erfolgreich?  " + annahmeGleichDouble(s.getDistance(Sensor.FL),8d));

    }

    public static boolean annahmeGleichDouble(double ist, double soll){
        if (ist == soll){
            return true;
            }
        else {
            return false;
        }
    }

    public static boolean annahmeGleichInt(int ist, int soll){
        if (ist == soll){
            return true;
        }
        else {
            return false;
        }
    }
}
