package fahrradbus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
/**
 *  erstellt und verwaltet die AGB-Szene.
 * @author Charlin
 *
 */
public class AGB_Bestätigung
{
	GridPane agbGrid = new GridPane();
	Scene agbSzene = new Scene(agbGrid, 600, 550);
	
	TextArea AGB = new TextArea ("\u2022 Die Straßenverkehrsordnung kenne ich und halte ich ein.\n"
			+ "\u2022 Ich behandele andere Verkehrsteilnehmer*innen mit besonderer Rücksicht.\n"
			+ "\u2022 Ich halte die Maximalgeschwindigkeit von 30 km/h ein.\n"
			+ "\u2022 Ich steuere den Fahrradbus ausschließlich von der linken Seite.\n"
			+ "\u2022 Ich prüfe vor Fahrtantritt Bremsen (Probe-Bremsung), Reifendruck, \nLenkung, Lichtanlage, Sitzschnellspanner der Sitze und Kupplung (Muttern gekontert?)\n"
			+ "\u2022 Ich gebe den Fahrradbus nicht an Dritte weiter.\n"
			+ "\u2022 Die Benutzung erfolgt auf eigene Gefahr.\n"
			+ "\u2022 Das Fahrradbus-Projekt übernimmt keine Haftung im Schadensfalle.\n"
			+ "\u2022 Eventuelle Schäden sind zu melden und werden dem Nutzer in Rechnung gestellt. \nSelbst- und fremdverschuldete Schäden sind durch den Nutzer zu begleichen. \nRückforderungen von Dritten sind durch den Nutzer eigenverantwortlich abzuwickeln.\n"
			+ "\u2022 Ich habe eine umfassende Sicherheitseinführung erhalten.\n(mehr dazu in der Bedienungsanleitung auf www.fahrradbus.com)\n"
			+ "\u2022 Ich habe an einer Theoriestunde und einer praktischen Fahrstunde teilgenommen \nsowie mindestens einmal die Wartungsarbeiten kennen gelernt und unterstützt.\n"
			+ "\u2022 Ich gebe Passagieren vor der Fahrt eine Sicherheitseinweisung (Mitbremsen, \nBeladung und Vorderradlast, richtiges Schalten) und weise insbesondere auf die \nnotwendige Aufmerksamkeit und das Mitbremsen hin.\n"
			+ "\u2022 Die Mitnahme von Passagieren erfolgt auf eigene Gefahr.\n"
			+ "\u2022 Ich werde den Fahrradbus zur angegebenen Zeit zurückgebracht haben.\n"
			+ "\u2022 Mir ist bekannt, dass das Fahrradbus-Projekt im Falle der Überziehung der Leihzeit \neine Anzeige wegen Diebstahlsl gegen mich aufgeben kann.");
		
	
	Text fehlermeldung = new Text("Bitte bestätige die AGB!");
	
	CheckBox bestätigung = new CheckBox("Ich habe die AGB gelesen und bin mit den Bedingungen einverstanden.");
	
	Button weiter = new Button("Weiter");
	
	//Insets rand = new Insets(20, 0, 20, 20);
	
	/**
	 * stellt die Szene AGB-Bestätigung zusammen.
	 * @return AGB-Szene
	 */
	public Scene getScene() {		
		agbSzene.getStylesheets().add("file:fahrradbus.css");
		weiter.setPrefSize(150,40);
		//weiter.setStyle("-fx-background-color: #194ea0; -fx-text-fill: #7892ba");
		AGB.setPrefSize(560, 400);
				
		agbGrid.add(AGB, 0, 0);
		agbGrid.addRow(1, bestätigung);
		agbGrid.addRow(2, weiter);
		
		//agbGrid.setStyle("-fx-background-color: white");
		//agbGrid.setVgap(10);
		//agbGrid.setHgap(10);
		//agbGrid.setPadding(rand);
		
		fehlermeldung.setFill(Color.RED);
		
		return agbSzene;
	}
	
	/**
	 * Getter für die Bestätigungsbox
	 * @return Bestätigungs-Checkbox
	 */
	public CheckBox getBestätigungsBox() {
		return bestätigung;
	}
	/**
	 * Getter für die Fehlermeldung
	 * @return Textfeld für Fehlermeldung
	 */
	public Text getFehlermeldungsFeld() {
		return fehlermeldung;
	} 
	
	/**
	 * Getter für die GridPane der AGB-Szene
	 * @return GridPane der AGB-Szene
	 */
	public GridPane getGridPane() {
		return agbGrid;
	}
	
	/**
	 * Setter für den EventHandler des weiter-Buttons
	 * @param weiterhandler
	 */
	public void setHandler(EventHandler<ActionEvent> weiterhandler) {
		weiter.setOnAction(weiterhandler);
	}

}
