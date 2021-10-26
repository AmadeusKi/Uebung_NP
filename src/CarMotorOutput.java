/** Schnittstelle für Ausgabeeinheiten im Projekt zu Wissensverarbeitung WS17/18.
* Die Schnittstelle darf nur in Absprache mit dem Dozenten verändert werden.
*/
public interface CarMotorOutput{
	
	
	/** aendern der Geschwindigkeit, anhalten oder rueckwaerts fahren. 
	*@param x die neu aufzunehmende Geschwindigkeit. x=0 bedeutet, dass das Fahrzeug 
	* anhalten soll. Bei 0 < x <=100 faehrt das Auto vorwaerts, bei -100 <= x < 0 
	* rueckwaerts. Der Wert beschreibt die aufzunehmende Geschwindikeit in Prozent 
	* der Maximalgeschwindigkeit.
	* @throws CarException wird geworfen, wenn es Probleme gab und die Geschwindikeit 
	* nicht geaendetrt wird.
	*/
	void setSpeed(int x) throws CarException;
	
	/** lenken des Fahrzeugs. 
	* @param x der einzuschlagende Lenkwinkel in Prozet vom maximalen Lenkwinkel. 
	* Bei -100 <= x < 0 lenkt das Fahrzeug nach links, bei x=0 geradeaus und bei 
	* 0 < x <= 100 nach rechts.
	* @throws CarException wird geworfen, wenn es Probleme gab und die Lenkrichtung 
	* nicht geaendetrt wird.
	*/
	void steering(int x) throws CarException;
}