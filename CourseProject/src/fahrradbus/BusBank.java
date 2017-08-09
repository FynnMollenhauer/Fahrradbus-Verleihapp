package fahrradbus;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Erzeugt, speichert und verwaltet die Busdaten.
 * @author Charlin
 *
 */
public class BusBank {
	Hashtable<Integer, String> busse = new Hashtable<Integer, String>();

	/**
	 * Legt die Busse an, falls die BusBank noch nicht existiert.
	 * @throws Exception
	 */
	void busseAnlegen() throws Exception {
		busse.put(1, "");
		busse.put(2, "");
		busse.put(3, "");
		
		Path daten = Paths.get(PfadKonfiguration.pfad + "BusDatenbank.dat");
		//Wenn das File noch nicht existiert, Erstellen des Files und Abspeichern der Busse aus
		//der Hashtable busse.
		if (!Files.exists(daten)) {
			Files.createFile(daten);
			OutputStream output = Files.newOutputStream(daten);
			ObjectOutputStream oos = new ObjectOutputStream(output);
			
			oos.writeObject(busse);
		}
	}
	
	/**
	 * Laden der Daten in die Hashtable busse.
	 * @throws Exception
	 */
	void datenLaden() throws Exception {
		Path daten = Paths.get(PfadKonfiguration.pfad + "BusDatenbank.dat");
		if (Files.exists(daten)) {
			InputStream input = Files.newInputStream(daten);
			ObjectInputStream ois = new ObjectInputStream(input);
			busse = (Hashtable<Integer, String>) ois.readObject();
		}
	}
	
	/**
	 * Abspeichern der Busse aus
	 * der Hashtable busse.
	 * @throws Exception
	 */
	void datenSpeichern() throws Exception {
		Path daten = Paths.get(PfadKonfiguration.pfad + "BusDatenbank.dat");
		if (!Files.exists(daten)) {
			Files.createFile(daten);
		} else {
			Files.deleteIfExists(daten);
			Files.createFile(daten);
		}
		OutputStream output = Files.newOutputStream(daten);
		ObjectOutputStream oos = new ObjectOutputStream(output);
		
		oos.writeObject(busse);
	}
	
	/**
	 * Hinzufügen der gebuchten Tage zu den geblockten Tagen eines Busses.
	 * @param nummer des Busses
	 * @param tage gebuchte Tage
	 */
	void geblockteZeitÄndern(Integer nummer, String tage) {
		String geblockteTage = busse.get(nummer);
		//wenn es noch keine geblockten Tage gibt, darf das Trennzeichen ":" nicht hinzugefügt werden
		if (geblockteTage.equals("")) {
			geblockteTage = tage;
		} else {
			geblockteTage = geblockteTage + ":" + tage;
		}
		//geblockte Tage in Hashtable ersetzen.
		busse.replace(nummer, geblockteTage);
	}
	
	/** 
	 * Gibt uns eine Liste der Busse als Bus-Objekte zurück.
	 * @return Busse als Bus-Objekte
	 */
	List<Bus> getBusse() {
		List<Bus> busListe = new ArrayList<Bus>();
		//Erstellen der Bus-Objekte und Hinzufügen zur Liste.
		for (int i = 1; i <= busse.size(); i++) {
			busListe.add(new Bus(i, busse.get(i)));
		}
		return busListe;
	}
}
