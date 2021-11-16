import java.util.HashMap;

public class Controller{

    private CarSensorInput cs;
    private CarMotorOutput cm;
    private HashMap<CarSensorInput.Sensor, Double> sensorMessWerte = new HashMap<>();
    private int currentSpeed = 999;
    private int currentSteering = 999;

    public HashMap<CarSensorInput.Sensor, Double> getSensorMessWerte() {
        return sensorMessWerte;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public int getCurrentSteering() {
        return currentSteering;
    }



    Controller(CarSensorInput cs, CarMotorOutput cm){
        this.cm = cm;
        this.cs = cs;

        //Erstinitialisierung der Hashmap mit Anfangswerten
        sensorMessWerte.put(CarSensorInput.Sensor.FL, 50d);
        sensorMessWerte.put(CarSensorInput.Sensor.FR, 60d);
        sensorMessWerte.put(CarSensorInput.Sensor.BL, 70d);
        sensorMessWerte.put(CarSensorInput.Sensor.BR, 80d);


    }
    //Klasse zum Starten der Threads in denen die Sensorwerte abgefragt und bei signifikanter Änderung
    //an den Controller geschickt werden
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
//                        System.out.println("Messwert von " + s.toString() + " geändert.");
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

    //Methoden zum starten der threads
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

    //Logik zur Verarbeitung der Sensorwerte
    public void control(HashMap<CarSensorInput.Sensor, Double> sensorMessWerte) throws CarException {

        if (sensorMessWerte.get(CarSensorInput.Sensor.FL) > 10 && sensorMessWerte.get(CarSensorInput.Sensor.FR) > 10){
            currentSpeed = 100;
            cm.setSpeed(100);
            //System.out.println("Freie Fahrt");
        }
        if (sensorMessWerte.get(CarSensorInput.Sensor.FL) < 10 && sensorMessWerte.get(CarSensorInput.Sensor.FR) < 10){
            currentSpeed = 0;
            cm.setSpeed(0);
            //System.out.println("Gefahrenbremsung");
        }
        if (sensorMessWerte.get(CarSensorInput.Sensor.FL) < 10 && sensorMessWerte.get(CarSensorInput.Sensor.BL) < 10){
            currentSteering = 100;
            cm.steering(100);
            //System.out.println("Gefahrenbremsung");
        }

    }

    //Stopperklasse um alle Threads zu stoppen.
    class Stopper extends Thread{
        SensorThread thread;

        Stopper(SensorThread t){thread = t;}
        public void run(){
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thread.setContinue(false);
        }
    }

}
