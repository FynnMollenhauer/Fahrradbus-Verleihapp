package fahrradbus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
/**
 * erster Screen zur Information über allgemeine Nutzung des Fahrradbusses.
 * @author Charlin
 *
 */

public class VorIntroScreen {

	
	GridPane vorIntroGrid;
	Scene vorIntroSzene;
	
//Einfügen des Unternehmenslogos
	Image header = new Image("Header_FaB04.jpg");
	ImageView headerView = new ImageView("Header_FaB04.jpg");
	
	Button zurAnmeldung = new Button("Zur Anmeldung");
	
//Textfeld mit Erläuterungen zur Nutzung des Fahrradbusses
	TextArea infos = new TextArea("„Der Fahrradbus ist ein Nebeneinander für mehrere Personen\nund Pilot-Projekt für konvivionale Technik. Es entsteht ein Raum für offenen Dialog,\nherzhaftes Lachen und echte Muskelkraft. Mit dem Fahrradbus fahren wir gemeinsam\nin eine zukunftsfähige Mobilitätsstruktur.“\n\n"
			+ "DEIN WEG ZUM FAHRRADBUS\n\n"
			+ "Bevor du alle Vorteile des Fahrradbusses nutzen kannst,\nist das Ablegen einer zweistündigen kostenlosen theoretischen und praktischen\nFahrradprüfung mit anschließender Ausgabe deiner persönlichen Zugangsdaten\nfür einen sicheren und nachhaltigen Gebrauch erforderlich.\n\n"
			+ "Falls dies noch nicht geschehen ist, kannst du dich unter\nfolgender E-Mail Adresse für die Fahrradprüfung anmelden: info@fahrradbus.com.\nBitte gib dabei Vor-, Nachname, eine Kontaktmöglichkeit sowie einen der\nfolgenden Termine an.\n\n"
			+ "Mögliche Termine für deine Fahrradprüfung:\n\n"
			+ "   - Montag, 03.07.2017, 18:00 – 20:00 Uhr\n"
			+ "   - Montag, 17.07.2017, 18:00 – 20:00 Uhr\n"
			+ "   - Montag, 01.08.2017, 18:00 – 20:00 Uhr\n"
			+ "   - Montag, 14.08.2017, 18:00 – 20:00 Uhr\n\n"
			+ "Nach erfolgreich bestandener Fahrradprüfung können die drei vorhandenen Fahrradbusse\n jederzeit nach jeweiliger Verfügbarkeit, welche im Kalender einsehbar ist,\ngebucht und genutzt werden. "
			+ "Nach erfolgreicher Buchung erhältst du von uns\neine Bestätigungs-E-Mail mit deinem aktuellen Zahlenschloss-Code,\nwelcher für jede Nutzung neu vergeben wird. Der Fahrradbus steht dann zur gebuchten Zeit\nan einem festen Standort für dich zur Verfügung.\n\n"
			+ "Standort: Am Sande 1\n\n"
			+ "Zeitbuchung: Abholung ab 7:00 Uhr, Abgabe bis 23:00 Uhr,\nmaximal zwei Tage hintereinander buchbar.\n\n"
			+ "Bitte achte darauf, den Fahrradbus rechtzeitig nach Ablauf der Buchungsdauer\nwieder mit dem Fahrradschloss gesichert am oben angegebenen Standort\nzur Verfügung zu stellen, sodass eine reibungslose, unkomplizierte Übergabe\nan weitere Nutzer stattfinden kann.\n\n"
			+ "Für eine ordentliche, verantwortungsvolle und nachhaltige Nutzung\nder Fahrradbusse sind wir dir sehr dankbar.\n\n"
			+ "Weitere (rechtliche) Informationen findest du in den AGB’s sowie auf unserer Homepage.\n\n"
			+ "Sollten dennoch weitere Fragen auftauchen,\nstehen wir gerne per E-Mail (info@fahrradbus.com) oder in Notfällen auch telefonisch\nunter folgender Nummer zur Verfügung: 0176 3039 1523\n\n"
			+ "Dein Fahrradbus-Team!");
	//CSS!!!!!
	Insets rand = new Insets(20, 0, 20, 20);
	/**
	 * Methode, um die Szene "VorIntroScreen" im Buchungsmanager aufrufen zu können.
	 * @return
	 */
	public Scene getScene() {
		
		vorIntroGrid = new GridPane();
		vorIntroSzene = new Scene(vorIntroGrid, 630, 800);
		
	
		infos.setPrefSize(590, 600);
		infos.setEditable(false);
		zurAnmeldung.setStyle("-fx-background-color: #194ea0; -fx-text-fill: #7892ba");
		zurAnmeldung.setPrefSize(150, 40);
		
		vorIntroGrid.addColumn(0, infos);
		vorIntroGrid.addRow(1, zurAnmeldung);
		vorIntroGrid.setStyle("-fx-background-color: white");
		vorIntroGrid.setVgap(10);
		vorIntroGrid.setHgap(10);
		vorIntroGrid.setPadding(rand);
		
		return vorIntroSzene;
	}
	

	public void setEventHandler(EventHandler<ActionEvent> handler) {
		zurAnmeldung.setOnAction(handler);
	}

}
