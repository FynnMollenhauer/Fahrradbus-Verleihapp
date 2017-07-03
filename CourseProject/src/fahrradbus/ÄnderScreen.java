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

public class ÄnderScreen {
	
	GridPane änderPane;
	Scene änderSzene;
	
	Image header1 = new Image("Header_FaB04.jpg");
	ImageView headerView1 = new ImageView("Header_FaB04.jpg");
	
	TextField email1 = new TextField("E-Mail-Adresse");
	PasswordField password1 = new PasswordField();
	
	PasswordField passwordNeu = new PasswordField();
	PasswordField passwordNeu1 = new PasswordField();
	
	Button prüfen = new Button("Passwort überprüfen");
	Button verbindlichÄndern = new Button("Passwort verbindlich ändern");
	Button zurück = new Button("Zurück zur Anmeldung");
		
	final UserBank datenbank = new UserBank();
	
	public Scene getScene() {
		try {
			datenbank.datenLaden();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		änderPane = new GridPane();
		änderSzene = new Scene(änderPane, 430, 500);
		
		prüfen.setPrefSize(150, 40);
		prüfen.setStyle("-fx-background-color: #194ea0; -fx-text-fill: #7892ba");
		verbindlichÄndern.setDisable(true);
		
		zurück.setPrefSize(230, 40);
		zurück.setStyle("-fx-background-color: #194ea0; -fx-text-fill: #7892ba");

		email1.setStyle("-fx-text-fill: grey");
		password1.setStyle("-fx-text-fill: grey");
		password1.setPromptText("Passwort");
	
		passwordNeu.setStyle("-fx-text-fill: grey");
		passwordNeu.setPromptText("Passwort");
		passwordNeu1.setStyle("-fx-text-fill: grey");
		passwordNeu1.setPromptText("Passwort");

		VBox combo1 = new VBox(0, email1, password1);
		VBox comboPassword = new VBox(0, passwordNeu, passwordNeu1);
		änderPane.add(headerView1, 0, 1);
		änderPane.addRow(2, combo1);
		änderPane.addRow(3, prüfen);
		änderPane.addRow(4, comboPassword);
		änderPane.addRow(5, verbindlichÄndern);
		änderPane.addRow(6, zurück);
		

		EventHandler<MouseEvent> emailEnter1 = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				email1.setText("");
				email1.setStyle("-fx-text-fill: black");
			}
		};
		email1.addEventHandler(MouseEvent.MOUSE_PRESSED, emailEnter1);

		EventHandler<MouseEvent> passwordEnter1= new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				password1.setPromptText("");
				password1.setStyle("-fx-text-fill: black");
			}
		};
		password1.addEventHandler(MouseEvent.MOUSE_CLICKED, passwordEnter1);
		passwordNeu.addEventHandler(MouseEvent.MOUSE_CLICKED, passwordEnter1);

		EventHandler<ActionEvent> überprüfen = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String e = email1.getText();
				String p = password1.getText();
				String prüfen1 = datenbank.nutzer.get(e);
				if (p.equals(prüfen1)) {
					System.out.println("Test..es funktioniert");
					verbindlichÄndern.setDisable(false);
				} else {
					System.out.println("es funktioniert nicht :(");
					email1.setStyle("-fx-text-fill: red");
					email1.setText("Falsches Passwort oder Benutzername!");
					password1.setText("");
				}
			}
		};
		prüfen.setOnAction(überprüfen);

		änderPane.setStyle("-fx-background-color: white");
		änderPane.setVgap(10);
		änderPane.setHgap(10);
		Insets rand = new Insets(20, 0, 20, 20);
		änderPane.setPadding(rand);
		return änderSzene;
	}
	
	public void passwortÄndern() {
		try {
			datenbank.datenLaden();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		datenbank.passwortÄndern(email1.getText(), passwordNeu.getText());
	}
	
	public void setÄndernEventHandler(EventHandler<ActionEvent> passwortÄndern) {
		verbindlichÄndern.setOnAction(passwortÄndern);
	}
	
	public void setZurückEventHandler(EventHandler<ActionEvent> zuückHandler) {
		zurück.setOnAction(zuückHandler);
	}
	
}
