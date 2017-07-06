package fahrradbus;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Regelt den Ablauf des Programms mit den unterschiedlichen Szenen und den Buchungsvorgang.
 * @author Charlin
 *
 */
public class BuchungsManager extends Application {
	
	private VorIntroScreen vorIntroScreen = new VorIntroScreen();
	private Intro intro = new Intro();
	private ÄnderScreen änderScreen = new ÄnderScreen();
	private Calendar calendar;
	private AGB_Bestätigung agb_Bestätigung = new AGB_Bestätigung();
	private Glückwunsch glückwunsch = new Glückwunsch();
	private PinManager pinManager = new PinManager();
	
	private BusBank busBank = new BusBank();
	private List<Bus> busse;
	private Scene scene;
	
	private String email;
	private String dates;
	private int anzahlPlätze;
	
	@Override
	public void start(final Stage hauptScreen) throws Exception {
		hauptScreen.setTitle("Fahrradbus");
		scene = vorIntroScreen.getScene();
		hauptScreen.setScene(scene);
		hauptScreen.show();
		
		//wenn im vorIntroScreen Enter gedrückt wird, wird die nächste Szene
		//Intro geöffnet.
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if (event.getCode() == KeyCode.ENTER) {
            	scene = intro.getScene();
            	hauptScreen.setScene(scene);
				hauptScreen.show();
            }
        });
		
		//Hier wird die Bus-Datenbank erstellt (wenn sie noch nicht existiert)
		busBank.busseAnlegen();
		//existierende Datenbank wird geladen
		busBank.datenLaden();
		//Busse mit den Informationen zu geblockten Tagen werden aus der BusBank geholt
		busse = busBank.getBusse();
		
		//calendar erzeugen, mit der Liste busse füllen
		calendar = new Calendar(busse);
		
		//Hier werden die Pincodes angelegt
		pinManager.pinsAnlegen();
		//Pins werden geladen
		pinManager.datenLaden();
		
		//Handler für Buttons, die zur Intro-Szene führen
		EventHandler<ActionEvent> zumIntro = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scene = intro.getScene();
				hauptScreen.setScene(scene);
				hauptScreen.show();
			}
		};
		vorIntroScreen.setEventHandler(zumIntro);
		änderScreen.setZurückEventHandler(zumIntro);
		
		//Handler für Button, der zur AGB-Bestätigungs-Szene führt
		EventHandler<ActionEvent> introWeiter = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//nur wenn E-Mail und Passwort in der UserBank vorhanden sind
				//und übereinstimmen, wird der Benutzer zur AGB-Szene weitergeleitet.
				if (intro.überprüfeDaten()) {
					email = intro.getEmail();
					hauptScreen.setScene(agb_Bestätigung.getScene());
					hauptScreen.show();
				}
			}
		};
		intro.setAnmeldenEventHandler(introWeiter);
		
		//Handler für Button, der zurück zum vorIntroScreen führt
		EventHandler<ActionEvent> introZurück = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scene = vorIntroScreen.getScene();
				//siehe Kommentar Zeile 51
				scene.addEventFilter(KeyEvent.KEY_PRESSED, event2->{
		            if (event2.getCode() == KeyCode.ENTER) {
		            	scene = intro.getScene();
		            	hauptScreen.setScene(scene);
						hauptScreen.show();
		            }
		        });
				hauptScreen.setScene(scene);
				hauptScreen.show();
			}
		};
		intro.setZurückEventHandler(introZurück);
		
		//Handler für Button, der zur Änder-Szene führt
		EventHandler<ActionEvent> passwortÄndern = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				änderScreen.passwortÄndern();
				hauptScreen.setScene(intro.getScene());
				hauptScreen.show();
			}
		};
		änderScreen.setÄndernEventHandler(passwortÄndern);
		
		//Handler, um von der AGB-Bestätigung zur Calendar-Szene zu kommen
		EventHandler<ActionEvent> agbAkzeptieren = new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				CheckBox bestätigung = agb_Bestätigung.getBestätigungsBox();
				Text fehlermeldung = agb_Bestätigung.getFehlermeldungsFeld();
				GridPane agbGrid = agb_Bestätigung.getGridPane();
				//wenn die CheckBox bestätigt wurde, wird die Calendar-Szene gezeigt
				if (bestätigung.isSelected() == true) {
					hauptScreen.setScene(calendar.getScene());
					hauptScreen.show();
				} 
				//sonst Fehlermeldung anzeigen
				else if (bestätigung.isSelected() == false) {
					agbGrid.addRow(3, fehlermeldung);
				} 
			}
			
		};
		agb_Bestätigung.setHandler(agbAkzeptieren);
		
		//Handler, um von der Intro-Szene zur Änder-Szene zu kommen
		EventHandler<ActionEvent> ändernButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hauptScreen.setScene(änderScreen.getScene());
                hauptScreen.show();
            }
        };
        intro.setÄndernEventHandler(ändernButton);
		
        //Handler, um den Buchungsvorgang durchzuführen und die Glückwunsch-
        //Szene anzuzeigen.
        EventHandler<ActionEvent> buchenButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//Aus der ComboBox der Calendar-Szene wird die Anzahl der gebuchten Plätze
            	//und ein String, der die gebuchten Tage enthält, geholt
                anzahlPlätze = calendar.getAnzahlPlätze();
                dates = calendar.getDates();
                
                // Blockieren der gebuchten Tage pro Bus, wobei mit der niedrigsten verfügbaren Busnummer
                //angefangen wird.
                List<Integer> freieBusse = getfreieBusse();
            	//Hälfte der in der ComboBox ausgewählten Plätze = Anzahl der
                //benötigten Bus-Module
                for (int i = 0; i < anzahlPlätze / 2 && i < freieBusse.size(); i++) {
            		busBank.geblockteZeitÄndern(freieBusse.get(i), dates);
            	}
                busse = busBank.getBusse();
                
                //Erzeugen des Texts mit den Pincodes.
                String pincodesFormatiert = getPincodes(freieBusse);
                
                try {
                	//Speichern der gebuchten Tage, sodass sie blockiert bleiben
					busBank.datenSpeichern();
				} catch (Exception e) {
					e.printStackTrace();
				}
                //Erstellen und Versenden der Buchungsmail
                AusleihMail ausleihMail = new AusleihMail(pincodesFormatiert, dates);
                ausleihMail.sendMail(email);
                
                //Glückwunsch-Szene wird angezeigt.
                hauptScreen.setScene(glückwunsch.getScene());
                hauptScreen.show();
            }
        };
        calendar.setBuchenEventHandler(buchenButton);
        
	}
	
	/**
	 * Es wird eine Liste mit den freien Bussen für die gewünschten Buchungstage
	 * erstellt.
	 * @return Liste freier Busse
	 */
	private List<Integer> getfreieBusse() {
		//Erstellen einer Liste, die alle Busse enthält; aus dieser
		//Liste werden die bereits belegten Busse rausgestrichen.
		List<Integer> freieBusse = new ArrayList<Integer>();
		freieBusse.add(1);
		freieBusse.add(2);
		freieBusse.add(3);
		
		// Für jeden Bus werden die geblockten Tage des Busses mit den gebuchten Tagen verglichen.
		for (Bus bus : busse) {
			//Aufteilen der in einen String gespeicherten Daten, damit sie verglichen werden können.
    		String[] geblockteTage = bus.zeitGeblockt.split(":");
    		String[] zuPrüfendeTage = dates.split(":");
    		//Nur wenn geblockte Tage vorhanden sind muss der Bus weiter geprüft werden.
    		if (!(geblockteTage.length == 1 && geblockteTage[0].equals(""))) {
    			//Anschauen jedes geblockten Tages
    			for (int i = 0; i < geblockteTage.length; i++) {
    				//Variable zum Prüfen ob bereits ein gebuchter Tag geblocked ist.
    				boolean blocked = false;
    				//Anschauen jedes gebuchten Tages
    				for (int j = 0; j < zuPrüfendeTage.length; j++) {
    					//Vergleichen ob der geblockte Tag gleich mit einen Buchungstag ist.
	    				if (geblockteTage[i].equals(zuPrüfendeTage[j])) {
	    					//Entfernen des Busses aus der Liste wenn er für die Tage bereits gebucht wurde.
	    					freieBusse.remove((Integer) bus.nummer);
	    					//Abbrechen des Vergleichs mit diesem Bus
	    					blocked = true;
	    					continue;
	    				}
    				}
    				if (blocked) {
    					//Abbrechen des Vergleichs mit diesem Bus
    					continue;
    				}
    			}
    		}
    	}
		return freieBusse;
	}
	
	/**
	 * nimmt die Pincodes der gebuchten Busse für die entsprechenden Kalenderwochen
	 * und formatisiert sie in einen Text.
	 * @param gebuchteBusse
	 * @return Text mit Pincodes
	 */
	private String getPincodes(List<Integer> gebuchteBusse) {
		String pincodes = "";
		//zum Holen der Kalenderwoche aus LocalDate
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int ersteKalenderwoche;
		int zweiteKalenderwoche;
		Bus bus;
		
		// Holen der gebuchten Busse
		for (int i = 0; i < anzahlPlätze / 2 && i < gebuchteBusse.size(); i++) {
			bus = busse.get(gebuchteBusse.get(i) - 1);
    		String[] gebuchteTage = dates.split(":");
    		int jahr = 1;
    		//Prüfen, ob der erste Tag der Buchung in diesem Jahr liegt oder im Nächsten.
    		if (LocalDate.parse(gebuchteTage[0]).getYear() == LocalDate.now().getYear()) {
    			jahr = 0;
    		}
    		//Holen der Kalenderwoche des ersten Buchungstags.
    		ersteKalenderwoche = LocalDate.parse(gebuchteTage[0]).get(woy);
    		//Holen der Kalenderwoche des letzten Buchunstages.
    		zweiteKalenderwoche = LocalDate.parse(gebuchteTage[gebuchteTage.length - 1]).get(woy);
    		//Falls die Buchung über zwei Kalenderwochen geht
    		if (zweiteKalenderwoche != ersteKalenderwoche) {
    			int zweitesJahr = 1;
    			//Prüfen, ob der letzte Tag der Buchung in diesem Jahr liegt oder im Nächsten.
    			if (LocalDate.parse(gebuchteTage[gebuchteTage.length - 1]).getYear() == LocalDate.now().getYear()) {
    				zweitesJahr = 0;
        		}
    			//Erzeugen des Textes für die Pincodes in der Ausleihmail.
    			pincodes = pincodes + "Für den Bus mit der Nummer " + bus.nummer + " benutzen sie in der ersten Woche den Code " + 
						pinManager.getPincode(bus.nummer, jahr, ersteKalenderwoche) + 
						" für die zweite Woche benutzen sie zum Abschließen bitte den Code " 
						+ pinManager.getPincode(bus.nummer, zweitesJahr, zweiteKalenderwoche) + "\n";
    		} 
    		//Falls der Buchungszeitraum nur eine Kalenderwoche umfasst
    		else {
    			//Erzeugen des Textes für die Pincodes in der Ausleihmail.
    			pincodes = pincodes + "Für den Bus mit der Nummer " + bus.nummer + " benutzen sie den Code " + 
    						pinManager.getPincode(bus.nummer, jahr, ersteKalenderwoche) + "\n";
    		}
    	}
		return pincodes;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
