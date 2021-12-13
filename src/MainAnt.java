public class MainAnt {
    public static void main(String[] args) throws CarException, InterruptedException {

//        CarSensorInput cs;
//        CarMotorOutput cm;
        Controller c;
//
//        if (args[0].equals("GUI") && args[1].equals("GUI")){
//            GUI cs = new GUI();
//            c = new Controller(cs, cs);
//            startThreads(c);
//        }

        if (args[0].equals("SiL")){
            SilTest cs = new SilTest();
            if (args[1].equals("SiL")){
                c = new Controller(cs, cs);
                startThreads(c);
                cs.startTest(c);
            }
            if (args[1].equals("EV3")){
                CarMotor cm = new CarMotor();
                c = new Controller(cs, cm);
                startThreads(c);
            }
        }





    }

    static void startThreads(Controller c) throws CarException {
        c.startThreads();
    }
}
