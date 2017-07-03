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
	
	private List<Pincode> pinListenBusse;
	
	public String getPincode(int busNummer, int jahr, int kalenderwoche) {
		return pinListenBusse.get(busNummer - 1).getPincode(jahr, kalenderwoche);
	}
	
	public void pinsAnlegen() throws Exception {
		pinListenBusse = new ArrayList<Pincode>();
		pinListenBusse.add(new Pincode());
		pinListenBusse.add(new Pincode());
		pinListenBusse.add(new Pincode());
		
		
		Path daten = Paths.get("/Users/Charlin/Desktop/PinDatenbank.dat");
		if (!Files.exists(daten)) {
			pinListenBusse.get(0).init();
			pinListenBusse.get(1).init();
			pinListenBusse.get(2).init();
			Files.createFile(daten);
			datenSpeichern();
		}
	}
	
	public void datenLaden() throws Exception {
		Path daten = Paths.get("/Users/Charlin/Desktop/PinDatenbank.dat");
		if (Files.exists(daten)) {
			BufferedReader writer = null;
			try {
			    writer = new BufferedReader( new FileReader("/Users/Charlin/Desktop/PinDatenbank.dat"));
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
	public void datenSpeichern() throws Exception {
		Path daten = Paths.get("/Users/Charlin/Desktop/PinDatenbank.dat");
		if (!Files.exists(daten)) {
			Files.createFile(daten);
		} else {
			Files.deleteIfExists(daten);
			Files.createFile(daten);
		}
		
		Path liste = Paths.get("/Users/Charlin/Desktop/PinListe.txt");
		if (!Files.exists(liste)) {
			Files.createFile(liste);
		} else {
			Files.deleteIfExists(liste);
			Files.createFile(liste);
		}
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter( new FileWriter("/Users/Charlin/Desktop/PinListe.txt"));
		    writer.write("   Bus 1   Bus 2   Bus 3\n");
			for (int j = 0; j <= 1; j++) {
				writer.write(j + "\n");
				for (int i = 0; i < 52; i++) {
					writer.write((i + 1) + "  " + pinListenBusse.get(0).getPincode(j, i + 1) + "   " + 
							pinListenBusse.get(1).getPincode(j, i + 1) + "   " + 
							pinListenBusse.get(2).getPincode(j, i + 1) + "\n");
				} 
			}
			writer.close();
			writer = new BufferedWriter( new FileWriter("/Users/Charlin/Desktop/PinDatenbank.dat"));
			for (Pincode pinliste : pinListenBusse) {
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
	
	public void nächstesJahr() {
		for (Pincode pins : pinListenBusse) {
			pins.nächstesJahr();
		}
		try {
			datenSpeichern();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
