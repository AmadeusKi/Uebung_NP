public class Main {

    public static void main(String[] args) throws CarException, InterruptedException {
//        GUI cs = new GUI();
//        SilTest cs = new SilTest();
//        Controller c = new Controller(cs, cs);
//
//        c.startThreads();
//        cs.startTest(c);
        CarSensorInput sensor;
        CarMotorOutput motor;

        if (args[0].equals("SiL")){
           sensor = new SilTest();
        }if (args[1].equals("SiL")){
            motor = new SilTest();

        }

        Controller c = new Controller(sensor, motor);

    }
}
