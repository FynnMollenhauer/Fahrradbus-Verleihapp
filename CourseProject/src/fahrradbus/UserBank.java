package fahrradbus;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

public class UserBank {
	Hashtable<String, String> nutzer = new Hashtable<String, String>();

	void nutzerAnlegen(String e, String p) {
		
		if (!nutzer.containsKey(e)) {
			nutzer.put(e, p);
			try {
				datenSpeichern();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	void datenLaden() throws Exception {
		Path daten = Paths.get("/Users/KundenDatenbank.dat");
		if (Files.exists(daten)) {
			InputStream input = Files.newInputStream(daten);
			ObjectInputStream ois = new ObjectInputStream(input);
			nutzer = (Hashtable<String, String>) ois.readObject();
		}
	}
	void datenSpeichern() throws Exception {
		Path daten = Paths.get("/Users/KundenDatenbank.dat");
		if (!Files.exists(daten)) {
			Files.createFile(daten);
		} else {
			Files.deleteIfExists(daten);
			Files.createFile(daten);
		}
		OutputStream output = Files.newOutputStream(daten);
		ObjectOutputStream oos = new ObjectOutputStream(output);
		
		oos.writeObject(nutzer);
	}
	void passwortÄndern(String e, String p) {
		nutzer.remove(e);
		nutzer.put(e, p);
		try {
			datenSpeichern();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
