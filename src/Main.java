public class Main {
    public static void main(String[] args) throws CarException {
        CarSensorInput cs = new Carsensor();
        CarMotorOutput cm = new CarMotor();

        Controller c = new Controller(cs, cm);

        c.chkSensorBR();
        c.chkSensorBL();
        c.chkSensorFR();
        c.chkSensorFL();

    }
}
