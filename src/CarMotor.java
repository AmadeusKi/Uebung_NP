public class CarMotor implements CarMotorOutput{
    int speed;
    int steering;
    @Override
    public void setSpeed(int x) throws CarException {
        speed = x;
        System.out.println("Carmotor speed");
    }

    @Override
    public void steering(int x) throws CarException {
        steering = x;
        System.out.println("Carmotor steering");
    }
}
