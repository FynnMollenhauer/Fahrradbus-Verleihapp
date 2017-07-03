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

public class BusBank {
	Hashtable<Integer, String> busse = new Hashtable<Integer, String>();

	void busseAnlegen() throws Exception {
		busse.put(1, "");
		busse.put(2, "");
		busse.put(3, "");
		
		Path daten = Paths.get("/Users/Charlin/Desktop/BusDatenbank.dat");
		if (!Files.exists(daten)) {
			Files.createFile(daten);
			OutputStream output = Files.newOutputStream(daten);
			ObjectOutputStream oos = new ObjectOutputStream(output);
			
			oos.writeObject(busse);
		}
	}
	void datenLaden() throws Exception {
		Path daten = Paths.get("/Users/Charlin/Desktop/BusDatenbank.dat");
		if (Files.exists(daten)) {
			InputStream input = Files.newInputStream(daten);
			ObjectInputStream ois = new ObjectInputStream(input);
			busse = (Hashtable<Integer, String>) ois.readObject();
		}
	}
	void datenSpeichern() throws Exception {
		Path daten = Paths.get("/Users/Charlin/Desktop/BusDatenbank.dat");
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
	void geblockteZeitÄndern(Integer nummer, String tage) {
		String geblockteTage = busse.get(nummer);
		if (geblockteTage.equals("")) {
			geblockteTage = tage;
		} else {
			geblockteTage = geblockteTage + ":" + tage;
		}
		busse.replace(nummer, geblockteTage);
	}
	List<Bus> getBusse() {
		List<Bus> busListe = new ArrayList<Bus>();
		for (int i = 1; i <= busse.size(); i++) {
			busListe.add(new Bus(i, busse.get(i)));
		}
		return busListe;
	}
}
