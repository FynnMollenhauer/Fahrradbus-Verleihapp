package fahrradbus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PinManager {
	
	/** 
	 * enthält die Pins für die folgenden zwei Jahre für alle drei Busobjekte
	 */
	private List<Pincode> pinListenBusse;
	
	/**
	 * sucht aus der Liste der Pins abgefragte Pins für Kalenderwoche und Bus raus
	 * @param busNummer
	 * @param jahr
	 * @param kalenderwoche
	 * @return Pincode zum ausgewählten Bus und der Kalenderwoche
	 */
	public String getPincode(int busNummer, int jahr, int kalenderwoche) {
		return pinListenBusse.get(busNummer - 1).getPincode(jahr, kalenderwoche);
	}
	
	/**
	 * legt eine neue ArrayList an, fügt drei verschiedene Pincode-Arrays hinzu.
	 * Speichert die Pincodes für jeden Bus der drei Objekte in der Liste in die Datei PinDatenbank.dat
	 * @throws Exception
	 */
	public void pinsAnlegen() throws Exception {
		pinListenBusse = new ArrayList<Pincode>();
		//lege drei Stellen auf der Arraylist an.
		pinListenBusse.add(new Pincode());
		pinListenBusse.add(new Pincode());
		pinListenBusse.add(new Pincode());
		
		
		Path daten = Paths.get(PfadKonfiguration.pfad + "PinDatenbank.dat");
		//wenn die Datei "PinDatenbank.dat" nicht existiert,
		//erzeuge drei unterschiedliche ArrayLists mit den Codes über zwei Jahre
		//für jeden Bus.
		if (!Files.exists(daten)) {
			pinListenBusse.get(0).init();
			pinListenBusse.get(1).init();
			pinListenBusse.get(2).init();
			Files.createFile(daten);
			datenSpeichern();
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void datenLaden() throws Exception {
		Path daten = Paths.get(PfadKonfiguration.pfad + "PinDatenbank.dat");
		if (Files.exists(daten)) {
			BufferedReader writer = null;
			try {
			    writer = new BufferedReader( new FileReader(PfadKonfiguration.pfad + "PinDatenbank.dat"));
				for (Pincode pinliste : pinListenBusse) {
					for (int j = 0; j < 2; j++) {
						String[] pins = new String[52];
						for (int i = 0; i < 52; i++) {
							pins[i] = writer.readLine();
						}
						pinliste.setPins(j, pins);
					}
				}

			} catch ( IOException e) {
			}
			finally {
			    try {
			        if ( writer != null)
			        writer.close( );
			    }
			    catch ( IOException e) {
			    }
			}
		}
	}
	
	/**
	 * Erstellt die Datei PinDatenbank.dat.
	 * 
	 * @throws Exception
	 */
	public void datenSpeichern() throws Exception {
		Path daten = Paths.get(PfadKonfiguration.pfad + "PinDatenbank.dat");
		//falls die Datei "PinDatenbank.dat" noch nicht existiert, erzeuge sie, sonst ersetze sie durch eine neue Datei
		if (!Files.exists(daten)) {
			Files.createFile(daten);
		} else {
			Files.deleteIfExists(daten);
			Files.createFile(daten);
		}
		
		Path liste = Paths.get(PfadKonfiguration.pfad + "PinListe.txt");
		//falls die Datei "PinListe.txt" noch nicht existiert, erzeuge sie. Sonst ersetze sie.
		if (!Files.exists(liste)) {
			Files.createFile(liste);
		} else {
			Files.deleteIfExists(liste);
			Files.createFile(liste);
		}
		BufferedWriter writer = null;
		try {
		    //erstelle writer, der in die Datei "PinListe.txt" schreibt
			writer = new BufferedWriter( new FileWriter(PfadKonfiguration.pfad + "PinListe.txt"));
		    //Schreibe: 
		    writer.write("   Bus 1   Bus 2   Bus 3\n");
			// mit j = 0, danach mit j= 1: (Übergabe für aktuelles und nächstes Jahr)
		    for (int j = 0; j <= 1; j++) {
				//schreibe j, dann Zeilenumbruch
		    	writer.write(j + "\n");
				//Wiederhole 52 mal:
				for (int i = 0; i < 52; i++) {
					//Schreibe: Kalenderwoche, Pincode für ersten Bus in dieser Woche, Pincode für zweiten 
					//Bus in dieser Woche, Pincode für dritten Bus in dieser Woche. 
					writer.write((i + 1) + "  " + pinListenBusse.get(0).getPincode(j, i + 1) + "   " + 
							pinListenBusse.get(1).getPincode(j, i + 1) + "   " + 
							pinListenBusse.get(2).getPincode(j, i + 1) + "\n");
				} 
			}
			writer.close();
			//erstelle Writer, der in die Datei "PinDatenbank.dat" schreibt
			writer = new BufferedWriter( new FileWriter(PfadKonfiguration.pfad + "PinDatenbank.dat"));
			//für jedes Objekt der Liste pinListenBusse:
			for (Pincode pinliste : pinListenBusse) {
				//für j = 0, dann für j = 1:
				for (int j = 0; j < 2; j++) {
					for (int i = 1; i <= 52; i++) {
						writer.write(pinliste.getPincode(j, i) + "\n");
					}
				}
			}

		} catch ( IOException e) {
		}
		finally {
		    try {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e) {
		    }
		}
	}
	/**
	 * kann bei Jahreswechsel ausgeführt werden. Verschiebt die Pins von nächstem Jahr in
	 * dieses Jahr und legt neue zufällige Pins für nächstes Jahr aus.
	 * Dies wird für alle drei Busobjekte in der pinListenBusse ausgeführt.
	 */
	public void nächstesJahr() {
		//für jedes Objekt aus der Liste pinListenBusse...
		for (Pincode pins : pinListenBusse) {
			//führe die Methode nächstesJahr für die Pincodes aus
			pins.nächstesJahr();
		}
		try {
			datenSpeichern();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
