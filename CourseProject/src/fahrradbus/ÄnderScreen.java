package fahrradbus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
/**
 * Erstellt und verwaltet die Änder-Szene.
 * Setzt Passwort-Änderungen durch den User um. 
 * @author Charlin
 *
 */
public class ÄnderScreen {
	
	GridPane änderPane;
	Scene änderSzene;
	
	Image header1 = new Image("Header_FaB04.jpg");
	ImageView headerView1 = new ImageView("Header_FaB04.jpg");
	
	TextField email1 = new TextField("E-Mail-Adresse");
	PasswordField aktuellespasswort = new PasswordField();
	
	PasswordField passwortNeu = new PasswordField();
	PasswordField passwortNeuBestätigung = new PasswordField();
	
	Button prüfen = new Button("Passwort überprüfen");
	Button verbindlichÄndern = new Button("Passwort verbindlich ändern");
	Button zurück = new Button("Zurück zur Anmeldung");
		
	UserBank datenbank = new UserBank();
	
	/**
	 * stellt die Szene Änderscreen zusammen.
	 * Setzt die Passwort-Änderung in EventHandler um.
	 * @return Änder-Szene
	 */
	public Scene getScene() {
		//Falls beim Daten Laden etwas schief geht, schreibt die Methode printStackTrace die zuletzt aufgerufenen Methoden
		//in die Konsole.
		try {
			datenbank.datenLaden();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		änderPane = new GridPane();
		änderSzene = new Scene(änderPane, 430, 500);
		
		änderSzene.getStylesheets().add("file:fahrradbus.css");
		
		prüfen.setPrefSize(150, 40);
		verbindlichÄndern.setDisable(true);
		
		zurück.setPrefSize(230, 40);

		aktuellespasswort.setPromptText("Passwort");
	
		passwortNeu.setPromptText("Passwort");
		passwortNeuBestätigung.setPromptText("Passwort");

		VBox combo1 = new VBox(0, email1, aktuellespasswort);
		VBox comboPassword = new VBox(0, passwortNeu, passwortNeuBestätigung);
		änderPane.add(headerView1, 0, 1);
		änderPane.addRow(2, combo1);
		änderPane.addRow(3, prüfen);
		änderPane.addRow(4, comboPassword);
		änderPane.addRow(5, verbindlichÄndern);
		änderPane.addRow(6, zurück);
		
		//setzt das E-Mail-Textfeld zurück, wenn es angeklickt wird.
		EventHandler<MouseEvent> emailEnter1 = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				email1.setText("");
				email1.getStyleClass().add("enteredTextField");
			}
		};
		email1.addEventHandler(MouseEvent.MOUSE_PRESSED, emailEnter1);

		//Hier wird durch einen EventHandler überprüft, ob E-Mail und Passwort übereinstimmen 
		//und in der UserBank vorhanden sind.
		EventHandler<ActionEvent> überprüfen = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String e = email1.getText();
				String p = aktuellespasswort.getText();
				String prüfen1 = datenbank.nutzer.get(e);
				//Hier wird geprüft, ob sie übereinstimmen
				if (p.equals(prüfen1)) {
					System.out.println("Test..es funktioniert");
					//wenn sie übereinstimmen, wird der verbindlichÄndern-Button freigeschaltet.
					verbindlichÄndern.setDisable(false);
				} else {
					//wenn sie nicht übereinstimmen oder nicht in der UserBank vorhanden sind,
					//wird eine Fehlermeldung eingeblendet.
					System.out.println("es funktioniert nicht :(");
					email1.getStyleClass().add("warnung");
					email1.setText("Falsches Passwort oder Benutzername!");
					aktuellespasswort.setText("");
				}
			}
		};
		prüfen.setOnAction(überprüfen);

		return änderSzene;
	}
	
	/**
	 * ändert das Passwort in der UserBank.
	 */
	public void passwortÄndern() {
		try {
			datenbank.datenLaden();
		} catch (Exception e) {
			e.printStackTrace();
		}
		datenbank.passwortÄndern(email1.getText(), passwortNeu.getText());
	}
	
	/**
	 * Setter für den EventHandler des verbindlichÄndern-Buttons
	 * @param passwortÄndern
	 */
	public void setÄndernEventHandler(EventHandler<ActionEvent> passwortÄndern) {
		verbindlichÄndern.setOnAction(passwortÄndern);
	}
	
	/**
	 * Setter für den EventHandler des zurück-Buttons
	 * @param zuückHandler
	 */
	public void setZurückEventHandler(EventHandler<ActionEvent> zuückHandler) {
		zurück.setOnAction(zuückHandler);
	}
	
}
