import java.util.HashMap;

public class SilTest implements CarSensorInput, CarMotorOutput {
	public static HashMap<CarSensorInput.Sensor, Double> sensorMessWerte = new HashMap<>();


	int testSpeed;
	int testSteering;
	private Controller c;

	SilTest(){
		sensorMessWerte.put(CarSensorInput.Sensor.V, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.H, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.R, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.L, 30d);
	}

	public void startTest(Controller c) throws CarException, InterruptedException {

		//Um das Auto is rund um genug Platz
		sensorMessWerte.put(CarSensorInput.Sensor.V, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.H, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.R, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.L, 30d);

		c.control(sensorMessWerte);
		if (annahmeGleichInt(testSpeed, 100) && annahmeGleichInt(testSteering, 0))
			System.out.println("Test 1 erfolgreich!");

		Thread.sleep(1000);


		//Das Auto fährt auf ein Hindernis zu
		for (int i = 1; i <= 11; i++){
			sensorMessWerte.put(Sensor.V, sensorMessWerte.get(Sensor.V) - i);
			Thread.sleep(500);
			c.control(sensorMessWerte);
		}

		c.control(sensorMessWerte);
		if (annahmeGleichInt(testSpeed, 50) && annahmeGleichInt(testSteering, 0))
			System.out.println("Test 2 erfolgreich!");

		Thread.sleep(1000);


		sensorMessWerte.put(CarSensorInput.Sensor.V, 10d);
		sensorMessWerte.put(CarSensorInput.Sensor.H, 10d);
		sensorMessWerte.put(CarSensorInput.Sensor.R, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.L, 30d);

		c.control(sensorMessWerte);
		Thread.sleep(1000);

		sensorMessWerte.put(CarSensorInput.Sensor.V, 5d);
		sensorMessWerte.put(CarSensorInput.Sensor.H, 5d);
		sensorMessWerte.put(CarSensorInput.Sensor.R, 30d);
		sensorMessWerte.put(CarSensorInput.Sensor.L, 30d);

		System.out.println("fertig");


	}

	@Override
	public double getDistance(Sensor s) throws CarException {
		return sensorMessWerte.get(s);
	}

	@Override
	public void setSpeed(int x) throws CarException {
		this.testSpeed = x;

	}

	@Override
	public void steering(int x) throws CarException {
		this.testSteering = x;

	}

	public static boolean annahmeGleichDouble(double ist, double soll) {
		if (ist == soll) {
			return true;
		} else {
			System.out.println("Der Wert beträgt: " + ist);
			return false;
		}
	}

	public static boolean annahmeGleichInt(int ist, int soll) {
		if (ist == soll) {
			return true;
		} else {
			System.out.println("Der Wert beträgt: " + ist);
			return false;
		}

	}
}
