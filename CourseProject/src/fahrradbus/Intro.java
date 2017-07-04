package fahrradbus;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Anmeldefenster, in das Email und Passwort eingetragen werden können.
 * Weiterleitung auf die Website, Passwort ändern und Passwort vergessen-Funktion.
 * Anlegen der nutzer-Datenbank.
 * @author Steffi
 *
 */
public class Intro {

	GridPane grid;
	Scene introSzene;

	Image header = new Image("Header_FaB04.jpg");
	ImageView headerView = new ImageView("Header_FaB04.jpg");

	TextField email = new TextField("E-Mail-Adresse");
	PasswordField password = new PasswordField();

	Button anmelden = new Button("Anmelden");
	Button vergessen = new Button("Passwort vergessen?");
	Button ändern = new Button("Passwort ändern");
	Button zurück = new Button("Zurück zur Infoseite");

	Label hinweis = new Label("Du erhältst dein Passwort beim Fahrradbus-Führerscheintest!");
	Label warnung = new Label("");

	//Insets rand = new Insets(20, 0, 20, 20);
	
	final UserBank datenbank = new UserBank();
	
	public Scene getScene() {
		// introSzene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// BEISPIEL!!!
		try {
			datenbank.datenLaden();
			datenbank.nutzerAnlegen("fynn@live.de", "passwort123");
			datenbank.nutzerAnlegen("test", "123");
			datenbank.nutzerAnlegen("charlin.rennekamp@gmx.de", "123");
			datenbank.nutzerAnlegen("ma-wendt@hotmail.de", "pups");
			datenbank.nutzerAnlegen("steffi.metzner@gmail.com", "123");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		grid = new GridPane();
		introSzene = new Scene(grid, 430, 450);
		introSzene.getStylesheets().add("file:fahrradbus.css");
		
		anmelden.setPrefSize(150, 40);
		zurück.setPrefSize(150, 40);
		//anmelden.setStyle("-fx-background-color: #194ea0; -fx-text-fill: #7892ba");
		vergessen.getStyleClass().add("unsichtbarButton");
		//setStyle("-fx-background-color: white; -fx-border-color: white; -fx-text-fill: blue");
		ändern.getStyleClass().add("unsichtbarButton");
		//setStyle("-fx-background-color: white; -fx-border-color: white; -fx-text-fill: blue");

		//email.setStyle("-fx-text-fill: grey");
		//password.setStyle("-fx-text-fill: grey");
		password.setPromptText("Passwort");
		warnung.setText("");

		VBox combo = new VBox(0, email, password);
		HBox buttons = new HBox(0, vergessen, ändern);
		HBox moreButtons = new HBox(92, zurück, anmelden);
		grid.add(headerView, 0, 1);
		grid.addRow(2, combo);
		grid.addRow(3, moreButtons);
		grid.addRow(4, buttons);
		grid.addRow(5, hinweis);
		grid.addRow(6, warnung);
		
		EventHandler<MouseEvent> emailEnter = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				email.setText("");
				email.getStyleClass().add("enteredTextField");
				//setStyle("-fx-text-fill: black");
			}
		};
		email.addEventHandler(MouseEvent.MOUSE_PRESSED, emailEnter);

		EventHandler<MouseEvent> passwordEnter = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				password.setPromptText("");
				password.getStyleClass().add("enteredTextField");
				//setStyle("-fx-text-fill: black");
			}
		};
		password.addEventHandler(MouseEvent.MOUSE_CLICKED, passwordEnter);

		EventHandler<ActionEvent> vergessenButton = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String e = email.getText();

				if (e.equals("")) {
					email.setStyle("-fx-text-fill: red");
					email.setText("Bitte E-Mail-Adresse eintragen!");
				} else {
					if (datenbank.nutzer.containsKey(e)) {
						String p = datenbank.nutzer.get(e);
						VergessenMail mail = new VergessenMail(e, p, "Dein Passwort");
						mail.sendMail(e);
						email.setStyle("-fx-text-fill: green");
						email.setText("Eine E-Mail" + " wurde an ihre E-Mail-Adresse gesandt!");
					} else {
						email.setStyle("-fx-text-fill: red");
						email.setText("E-Mail-Adresse wurde nicht im System gefunden!");
					}
				}
			}
		};
		vergessen.setOnAction(vergessenButton);
		
		EventHandler<MouseEvent> LogoClick = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				String url = "http://www.fahrradbus.com";

				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(new URI(url));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
				} else {
					Runtime runtime = Runtime.getRuntime();
					try {
						runtime.exec("xdg-open " + url);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		};
		headerView.addEventHandler(MouseEvent.MOUSE_CLICKED, LogoClick);
		
		//grid.setStyle("-fx-background-color: white");
		//grid.setVgap(10);
		//grid.setHgap(10);
		
		//grid.setPadding(rand);
		
		return introSzene;
	}

	public void setAnmeldenEventHandler(EventHandler<ActionEvent> introWeiter) {
		anmelden.setOnAction(introWeiter);
	}
	
	public void setÄndernEventHandler(EventHandler<ActionEvent> introÄndern) {
		ändern.setOnAction(introÄndern);
	}
	
	public void setZurückEventHandler(EventHandler<ActionEvent> introZurück) {
		zurück.setOnAction(introZurück);
	}
	
	/**
	 * gibt Text aus dem Email-Textfeld zurück
	 * @return
	 */
	public String getEmail() {
		return email.getText();
	}
	
	public boolean überprüfeDaten() {
		String e = email.getText();
		String p = password.getText();
		try {
			datenbank.datenLaden();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String prüfen1 = datenbank.nutzer.get(e);
		if ((p).equals(prüfen1)) {
			return true;
		}
		warnung.setText("Passwort oder E-mail falsch");
		warnung.setStyle("-fx-text-fill: red");
		return false;
	}

}
