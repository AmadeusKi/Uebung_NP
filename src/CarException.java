/** Exception Klasse für das Projekt in Wissensverarbeitung im WS17/18. 
* Sie können Spezialisierungen für verschiedene Fehlertypen realisieren
* oder die das Problem einfach direkt in dieser Exception kodieren. Sie dürfen die 
* Klasse natürlich beleibig anpassen.
*/

class CarException extends Exception{
	CarException(){ super();}
	CarException(String s){super(s);}
	CarException(Exception e){super(e);}
}