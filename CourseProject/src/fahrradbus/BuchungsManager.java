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
		hauptScreen.setScene(vorIntroScreen.getScene());
		hauptScreen.show();
		
		busBank.busseAnlegen();
		busBank.datenLaden();
		busse = busBank.getBusse();
		calendar = new Calendar(busse);
		
		pinManager.pinsAnlegen();
		pinManager.datenLaden();
		
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
		
		EventHandler<ActionEvent> introWeiter = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (intro.überprüfeDaten()) {
					email = intro.getEmail();
					hauptScreen.setScene(agb_Bestätigung.getScene());
					hauptScreen.show();
				}
			}
		};
		intro.setAnmeldenEventHandler(introWeiter);
		
		EventHandler<ActionEvent> introZurück = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				hauptScreen.setScene(vorIntroScreen.getScene());
				hauptScreen.show();
			}
		};
		intro.setZurückEventHandler(introZurück);
		
		EventHandler<ActionEvent> passwortÄndern = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				änderScreen.passwortÄndern();
				hauptScreen.setScene(intro.getScene());
				hauptScreen.show();
			}
		};
		änderScreen.setÄndernEventHandler(passwortÄndern);
		
		EventHandler<ActionEvent> agbAkzeptieren = new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				CheckBox bestätigung = agb_Bestätigung.getBestätigungsBox();
				Text fehlermeldung = agb_Bestätigung.getFehlermeldungsFeld();
				GridPane agbGrid = agb_Bestätigung.getGridPane();
				if (bestätigung.isSelected() == true) {
					hauptScreen.setScene(calendar.getScene());
					hauptScreen.show();
				} else if (agbGrid.getChildren().contains(fehlermeldung)) {
					fehlermeldung.setStyle("-fx-background-color: white");
					fehlermeldung.setStyle("-fx-background-color: red");
				} else if (bestätigung.isSelected() == false) {
					agbGrid.addRow(3, fehlermeldung);
				} 
			}
			
		};
		agb_Bestätigung.setHandler(agbAkzeptieren);
		
		EventHandler<ActionEvent> ändernButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hauptScreen.setScene(änderScreen.getScene());
                hauptScreen.show();
            }
        };
        intro.setÄndernEventHandler(ändernButton);
		
        EventHandler<ActionEvent> buchenButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                anzahlPlätze = calendar.getAnzahlPlätze();
                dates = calendar.getDates();
                
                // Busse Blocken
                List<Integer> freieBusse = getfreieBusse();
            	for (int i = 0; i < anzahlPlätze / 2 && i < freieBusse.size(); i++) {
            		busBank.geblockteZeitÄndern(freieBusse.get(i), dates);
            	}
                busse = busBank.getBusse();
                
                String pincodesFormatiert = getPincodes(freieBusse);
                
                try {
					busBank.datenSpeichern();
				} catch (Exception e) {
					e.printStackTrace();
				}
                AusleihMail ausleihMail = new AusleihMail(pincodesFormatiert, dates);
                ausleihMail.sendMail(email);
                
                hauptScreen.setScene(glückwunsch.getScene());
                hauptScreen.show();
            }
        };
        calendar.setBuchenEventHandler(buchenButton);
        
	}
	
	private List<Integer> getfreieBusse() {
		List<Integer> freieBusse = new ArrayList<Integer>();
		freieBusse.add(1);
		freieBusse.add(2);
		freieBusse.add(3);
		for (Bus bus : busse) {
    		String[] geblockteTage = bus.zeitGeblockt.split(":");
    		String[] zuPrüfendeTage = dates.split(":");
    		if (!(geblockteTage.length == 1 && geblockteTage[0].equals(""))) {
    			for (int i = 0; i < geblockteTage.length; i++) {
    				boolean blocked = false;
    				for (int j = 0; j < zuPrüfendeTage.length; j++) {
	    				if (geblockteTage[i].equals(zuPrüfendeTage[j])) {
	    					freieBusse.remove((Integer) bus.nummer);
	    					blocked = true;
	    					continue;
	    				}
    				}
    				if (blocked) {
    					continue;
    				}
    			}
    		}
    	}
		return freieBusse;
	}
	
	private String getPincodes(List<Integer> gebuchteBusse) {
		String pincodes = "";
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int ersteKalenderwoche;
		int zweiteKalenderwoche;
		Bus bus;
		for (int i = 0; i < anzahlPlätze / 2 && i < gebuchteBusse.size(); i++) {
			bus = busse.get(gebuchteBusse.get(i) - 1);
    		String[] gebuchteTage = dates.split(":");
    		int jahr = 1;
    		if (LocalDate.parse(gebuchteTage[0]).getYear() == LocalDate.now().getYear()) {
    			jahr = 0;
    		}
    		ersteKalenderwoche = LocalDate.parse(gebuchteTage[0]).get(woy);
    		zweiteKalenderwoche = LocalDate.parse(gebuchteTage[gebuchteTage.length - 1]).get(woy);
    		if (zweiteKalenderwoche != ersteKalenderwoche) {
    			int zweitesJahr = 1;
    			if (LocalDate.parse(gebuchteTage[gebuchteTage.length - 1]).getYear() == LocalDate.now().getYear()) {
    				zweitesJahr = 0;
        		}
    			pincodes = pincodes + "Für den Bus mit der Nummer " + bus.nummer + " benutzen sie in der ersten Woche den Code " + 
						pinManager.getPincode(bus.nummer, jahr, ersteKalenderwoche) + 
						" für die zweite Woche benutzen sie zum Abschließen bitte den Code " 
						+ pinManager.getPincode(bus.nummer, zweitesJahr, zweiteKalenderwoche) + "\n";
    		} else {
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
