import com.sun.xml.internal.bind.v2.TODO;

import java.util.HashMap;

public class Controller{

    private CarSensorInput cs;
    private CarMotorOutput cm;
    private HashMap<CarSensorInput.Sensor, Double> sensorMessWerte = new HashMap<>();

    public HashMap<CarSensorInput.Sensor, Double> getSensorMessWerte() {
        return sensorMessWerte;
    }

    Controller(CarSensorInput cs, CarMotorOutput cm){
        this.cm = cm;
        this.cs = cs;

        //Erstinitialisierung der Hashmap mit Anfangswerten
        sensorMessWerte.put(CarSensorInput.Sensor.FL, 100d);
        sensorMessWerte.put(CarSensorInput.Sensor.FR, 100d);
        sensorMessWerte.put(CarSensorInput.Sensor.BL, 100d);
        sensorMessWerte.put(CarSensorInput.Sensor.BR, 100d);


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
//                        System.out.println("Messwert von " + s.toString() + " geändert.");
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

    // Methode, welche neue Messwerte in die Hashmap einträgt
    // Zusätzlich werden die neuen Sensorwerte der Fahrlogik übergeben.
    public void newValue(CarSensorInput.Sensor s, double messWert) throws CarException {
        this.sensorMessWerte.put(s, messWert);
        control(this.sensorMessWerte);
    }

    // Methode zur Steuerung der Motoren
    public void control(HashMap<CarSensorInput.Sensor, Double> sensorMessWerte) throws CarException {

        applyRule(sensorMessWerte.get(CarSensorInput.Sensor.FL), sensorMessWerte.get(CarSensorInput.Sensor.FR), sensorMessWerte.get(CarSensorInput.Sensor.BL), sensorMessWerte.get(CarSensorInput.Sensor.BR));

    }

    // Methode welche das Fahrverhalten in Abhängigkeit der Sensorwerte bestimmt
    private void applyRule(Double fl, Double fr, Double bl, Double br) throws CarException {
        if (fl < 10 && fr < 10){ //Gefahrenbremsung
            cm.setSpeed(0);
            cm.steering(0);

            if (bl > 20 && br > 20){ //zurücksetzen
                cm.setSpeed(-100);
                cm.steering(0);
            }
        }
        if (fl > 10 && fr > 10){ //leicht beschleunigen
            cm.setSpeed(50);
            cm.steering(0);
            if (fl > 20 && fr > 20){ // Vollgas
                cm.setSpeed(100);
                cm.steering(0);
            }
        }
        if (fl < 10 && fr > 10){ //Lenkung nach rechts; halbe Geschwindigkeit
            cm.steering(100);
            cm.setSpeed(50);
        }
        if (fl > 10 && fr < 10){ //Lenkung nach links; halbe Geschwindigkeit
            cm.setSpeed(50);
            cm.steering(-100);
        }

    }



    //Stopperklasse um alle Threads zu stoppen.
//    class Stopper extends Thread{
//        SensorThread thread;
//
//        Stopper(SensorThread t){thread = t;}
//        public void run(){
//            try {
//                Thread.sleep(400);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            thread.setContinue(false);
//        }
//    }
//
}
