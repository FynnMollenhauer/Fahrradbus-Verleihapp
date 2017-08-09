package fahrradbus;

import java.security.SecureRandom;

/**
 * Erstellt zufällige Pincodes.
 * @author Charlin
 *
 */
public class Pincode {

	public String codeNeu;
	private String[] pinsDiesesJahr;
	private String[] pinsNächstesJahr;
	
	/** 
	 * gibt den String des Pincodes für die abgefragte Kalenderwoche zurück
	 * @param jahr: aktuelles oder dieses Jahr (0 oder 1)
	 * @param kalenderwoche: Zahl der zu prüfenden Kalenderwoche
	 * @return Pin für die abgefragte Kalenderwoche
	 */
	public String getPincode(int jahr, int kalenderwoche) {
		if (jahr == 0) {
			return pinsDiesesJahr[kalenderwoche - 1];
		}
		return pinsNächstesJahr[kalenderwoche - 1];
	}
	
	/**
	 * Im Fall des Jahreswechsels werden pinsNächstesJahr in die Variable pinsDiesesJahr verschoben
	 * und die initJahr-Funktion für das nächste Jahr ausgeführt (neue Codes für ein weteres Jahr erzeugt.
	 */
	public void nächstesJahr() {
		pinsDiesesJahr = pinsNächstesJahr;
		initJahr(1);
	}

	/**
	 * zufälligen Pin der Länge length erstellen
	 * @param length
	 * @return zufälligen Code der Länge length als String
	 */
	public String createNewPin(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder pass = new StringBuilder(length);
		//so oft wie length
		for (int i = 0; i < length; i++) {
			//hänge eine zufällige Zahl zwischen 0 und 10 an pass an.
			pass.append(random.nextInt(10));
		}
		return pass.toString();
	}
	/**
	 * Erzeugt und füllt zwei Arrays für mit vierstelligen Codes für jede Kalenderwoche eines Jahres
	 */
	public void init() {
		initJahr(0);
		initJahr(1);
	}
	
	/**
	 * 
	 * @param j: abzurufendes Jahr
	 * @param p: 
	 */
	public void setPins(int j, String[] p) {
		if (j == 0) {
			pinsDiesesJahr = p;
		} else {
			pinsNächstesJahr = p;
		}
	}
	
	/**
	 * Erstellt die String-Arrays pinsDiesesJahr und pinsNächstes Jahr,
	 * legt deren Größe auf 52 Felder fest, und füllt jedes Feld mit 
	 * einen zufälligen Pin, der 4 Stellen lang ist.
	 * @param j
	 */
	private void initJahr(int j) {
		//definiere ein String-Array pins
		String[] pins;
		//wenn der übergebene Parameter 0 ist
		if (j == 0) {
			//lege die Größe des pinsDiesesJahr-Arrays auf 52 Stellen fest 
			pinsDiesesJahr = new String[52];
			pins = pinsDiesesJahr;
		} else {
			//lege die Größe des pinsNächstesJahr-Arrays auf 52 Stellen fest
			pinsNächstesJahr = new String[52];
			pins = pinsNächstesJahr;
		}
		//Wiederhole 52 mal:
		for (int i = 0; i < 52; i++) {
			//erstelle einen neuen zufälligen Pin mit 4 Stellen an die Stelle i des Arrays
			pins[i] = createNewPin(4);
		}
	}
	
}
