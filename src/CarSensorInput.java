/** Schnittstelle für Eingabeeinheiten im Projekt bei einer 
* Implementierung mit Regelschleife.
* Die Schnittstelle darf nur in Absprache mit dem Dozenten verändert werden.
*/

public interface CarSensorInput{
	/** es gibt vier Sensoren: FL ist vorne links am Auto angebracht, FR vorne rechts, 
	* BL hinten links und BR hinten rechts.
	*/
	enum Sensor {V, H, R, L;}
	
	/** 
	* Liefert die aktuell gemessene Entfernung zum naechsten ortbaren Objekt des 
	* Sensors s. Die AusfÃ¼hrung kann bei der Realisierung mit dem NXTUltrasonicSensor
	* einige Zeit in Anspruch nehmen (vgl. Doku von Lejos)
	* @param s der Sensor, der abgefragt werden soll
	* @return die aktuell gemessene Entfernung zum naechsten ortbaren Objekt in cm???
	* @throws CarException, wenn kein gÃ¼ltiger Wert gelesen werden konnte
	*/
	double getDistance(Sensor s) throws CarException;
	
}