public class Carsensor implements CarSensorInput{
    @Override
    public double getDistance(Sensor s) throws CarException {
        return Math.random()*100;
    }


}
