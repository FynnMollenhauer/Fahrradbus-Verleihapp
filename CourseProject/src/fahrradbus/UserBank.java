package fahrradbus;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

/**
 * Methoden zum Anlegen und Verwalten der Userdaten.
 * @author Steffi
 *
 */

public class UserBank {
	/**
	 * die Hashtable nutzer ordnet zwei Strings einander zu 
	 */
	Hashtable<String, String> nutzer = new Hashtable<String, String>();
	
	/**
	 * neuen Nutzer mit Email und Passwort in der Hashtable nutzer anlegen und speichern
	 * @param e Email
	 * @param p Passwort
	 */
	public void nutzerAnlegen(String e, String p) {
		if (!nutzer.containsKey(e)) {
			nutzer.put(e, p);
			try {
				datenSpeichern();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Laden der Daten in die Hashtable nutzer (
	 * @throws Exception
	 */
	public void datenLaden() throws Exception {
		Path daten = Paths.get(PfadKonfiguration.pfad + "KundenDatenbank.dat");
		//Wenn das "daten"-File schon existiert, rufe die nutzer-Daten daraus ab
		if (Files.exists(daten)) {
			InputStream input = Files.newInputStream(daten);
			ObjectInputStream ois = new ObjectInputStream(input);
			nutzer = (Hashtable<String, String>) ois.readObject();
		}
	}
	
	/**
	 * Abspeichern der Nutzerdaten aus der Hashtable nutzer.
	 * @throws Exception
	 */
	public void datenSpeichern() throws Exception {
		Path daten = Paths.get(PfadKonfiguration.pfad + "KundenDatenbank.dat");
		//erstelle das daten-File, falls es noch nicht existiert
		if (!Files.exists(daten)) {
			Files.createFile(daten);
		} else { //sonst lösche das aktuelle daten-File und erstelle ein neues.
			Files.deleteIfExists(daten);
			Files.createFile(daten);
		}
		OutputStream output = Files.newOutputStream(daten);
		ObjectOutputStream oos = new ObjectOutputStream(output);
		
		oos.writeObject(nutzer);
	}
	
	/**
	 * überschreibe das alte daten-File mit den neuen Daten des Nutzers.
	 * @param e Email
	 * @param p Passwort
	 */
	public void passwortÄndern(String e, String p) {
		nutzer.remove(e);
		nutzer.put(e, p);
		try {
			datenSpeichern();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
