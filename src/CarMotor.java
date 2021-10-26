public class CarMotor implements CarMotorOutput{
    int speed;
    int steering;
    @Override
    public void setSpeed(int x) throws CarException {
        speed = x;
    }

    @Override
    public void steering(int x) throws CarException {
        steering = x;
    }
}
