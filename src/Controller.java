import java.util.HashMap;

public class Controller{

    CarSensorInput cs;
    CarMotorOutput cm;
    HashMap<CarSensorInput.Sensor, Double> sensorMessWerte = new HashMap<>();

    Controller(CarSensorInput cs, CarMotorOutput cm){
        this.cm = cm;
        this.cs = cs;

        //Erstinitialisierung der Hashmap mit Anfangswerten
        sensorMessWerte.put(CarSensorInput.Sensor.FL, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.FR, 60d);
        sensorMessWerte.put(CarSensorInput.Sensor.BL, 70d);
        sensorMessWerte.put(CarSensorInput.Sensor.BR, 80d);


    }

    class SensorThread extends Thread{

        double messWert = 0;
        double letzterWert = 999;
        double schwellWert = 3;
        CarSensorInput.Sensor s;
        boolean cont = true;

        SensorThread(CarSensorInput.Sensor s) throws CarException {
            this.s = s;
        }

        public void run(){
            new Stopper(this).start();
            while (cont){
                try {
                    messWert = cs.getDistance(s);
                } catch (CarException e) {
                    e.printStackTrace();
                }
                if (Math.abs(letzterWert-messWert) > schwellWert){
                    try {
                        newValue(s, messWert);
                    } catch (CarException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setContinue(boolean b) {
            this.cont = b;
        }
    }


    public void chkSensorFL() throws CarException {
        SensorThread fl = new SensorThread(CarSensorInput.Sensor.FL);
        fl.start();
    }

    public void chkSensorFR() throws CarException {
        SensorThread fr = new SensorThread(CarSensorInput.Sensor.FR);
        fr.start();
    }

    public void chkSensorBL() throws CarException {
        SensorThread bl = new SensorThread(CarSensorInput.Sensor.BL);
        bl.start();
    }

    public void chkSensorBR() throws CarException {
        SensorThread br = new SensorThread(CarSensorInput.Sensor.BR);
        br.start();
    }

    public void newValue(CarSensorInput.Sensor s, double messWert) throws CarException {
        this.sensorMessWerte.put(s, messWert);
        control(this.sensorMessWerte);
    }

    public void control(HashMap<CarSensorInput.Sensor, Double> sensorMessWerte) throws CarException {

        if (sensorMessWerte.get(CarSensorInput.Sensor.FL) > 10 && sensorMessWerte.get(CarSensorInput.Sensor.FR) > 10){
            cm.setSpeed(100);
            System.out.println("Freie Fahrt");
        }
        if (sensorMessWerte.get(CarSensorInput.Sensor.FL) < 10 && sensorMessWerte.get(CarSensorInput.Sensor.FR) < 10){
            cm.setSpeed(0);
            System.out.println("Gefahrenbremsung");
        }

    }

    //Stopperklasse um alle Threads zu stoppen.
    class Stopper extends Thread{
        SensorThread thread;

        Stopper(SensorThread t){thread = t;}
        public void run(){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thread.setContinue(false);
        }
    }

}
