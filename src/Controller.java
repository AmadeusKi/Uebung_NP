import java.util.HashMap;

public class Controller{

    private CarSensorInput cs;
    private CarMotorOutput cm;
    private HashMap<CarSensorInput.Sensor, Double> sensorMessWerte = new HashMap<>();
    private int[] motorParameter;

    public HashMap<CarSensorInput.Sensor, Double> getSensorMessWerte() {
        return sensorMessWerte;
    }

    Controller(CarSensorInput cs, CarMotorOutput cm) throws CarException {
        this.cm = cm;
        this.cs = cs;

        //Erstinitialisierung der Hashmap mit Anfangswerten
        sensorMessWerte.put(CarSensorInput.Sensor.V, 100d);
        sensorMessWerte.put(CarSensorInput.Sensor.H, 100d);
        sensorMessWerte.put(CarSensorInput.Sensor.R, 100d);
        sensorMessWerte.put(CarSensorInput.Sensor.L, 100d);

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
            while (cont){
                try {
                    messWert = cs.getDistance(s);
                } catch (CarException e) {
                    e.printStackTrace();
                }
                if (Math.abs(letzterWert-messWert) > schwellWert){
                    try {
                        newValue(s, messWert);
                        letzterWert = messWert;
                    } catch (CarException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Methoden zum starten der Threads
    public void startThreads() throws CarException {
        SensorThread fl = new SensorThread(CarSensorInput.Sensor.V);
        fl.start();

        SensorThread fr = new SensorThread(CarSensorInput.Sensor.H);
        fr.start();

        SensorThread bl = new SensorThread(CarSensorInput.Sensor.R);
        bl.start();

        SensorThread br = new SensorThread(CarSensorInput.Sensor.L);
        br.start();
    }

    // Methode, welche neue Messwerte in die Hashmap einträgt
    // Zusätzlich werden die neuen Sensorwerte der Fahrlogik übergeben.
    public void newValue(CarSensorInput.Sensor s, double messWert) throws CarException {
        this.sensorMessWerte.put(s, messWert);
        control(this.sensorMessWerte);
    }

    // Methode zur Steuerung der Motoren
    public void control(HashMap<CarSensorInput.Sensor, Double> sensorMessWerte) throws CarException {

        motorParameter = new int[2];    // enthält die Parameter für den Motor Index 0 = Speed Index 1 = Steering
        motorParameter = applyRule(sensorMessWerte.get(CarSensorInput.Sensor.V), sensorMessWerte.get(CarSensorInput.Sensor.H), sensorMessWerte.get(CarSensorInput.Sensor.R), sensorMessWerte.get(CarSensorInput.Sensor.L));
        cm.setSpeed(motorParameter[0]);
        cm.steering(motorParameter[1]);
    }

    // Methode welche das Fahrverhalten in Abhängigkeit der Sensorwerte bestimmt
    private int[] applyRule(Double v, Double h, Double r, Double l) throws CarException {
        int[] param = new int[2];
        param[0] = 0;   //speed
        param[1] = 0;   //steering
        if (v < 5){ //Gefahrenbremsung
            param[0] = 0;
            param[1] = 0;

            if (h > 20){ //zurücksetzen
                param[0] = -100;
                param[1] = 0;
            }
            return param;
        }
        if (v > 10){ //leicht beschleunigen
            param[0] = 50;
            param[1] = 0;

            if (v > 20) { // Vollgas
                param[0] = 100;
                param[1] = 0;
            }
            return param;
        }
        if (v < 10 && l < 10){ //Lenkung nach rechts; halbe Geschwindigkeit
            param[0] = 50;
            param[1] = 100;
            return param;
        }
        if (v < 10 && r < 10){ //Lenkung nach links; halbe Geschwindigkeit
            param[0] = 50;
            param[1] = -100;
            return param;
        }
        return param;

    }
}
