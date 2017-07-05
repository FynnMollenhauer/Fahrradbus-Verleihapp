package fahrradbus;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Glückwunsch {

	Pane glückPane = new Pane();
	Scene glückwunschSzene = new Scene(glückPane, 365, 250);

	Image header = new Image("Header_FaB04_02.jpg");
	ImageView headerView = new ImageView("Header_FaB04_02.jpg");

	Label glück = new Label(
			"Glückwunsch zur Reservierung!\nDu erhältst alle näheren Infos die du\nzum Ausleihen brauchst per E-mail.\nDen Fahrradbus findest du Am Sande 1.");

	//Insets rand = new Insets(20, 0, 20, 20);

	public Scene getScene() {
		glückwunschSzene.getStylesheets().add("file:fahrradbus.css");
		
		glück.setLayoutX(70);
		glück.setLayoutY(165);
		headerView.setLayoutX(20);
		headerView.setLayoutY(40);

		glückPane.getChildren().add(glück);
		glückPane.getChildren().add(headerView);

		EventHandler<MouseEvent> LogoClick = new EventHandler<MouseEvent>() {
			@Override
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

		//glückPane.setStyle("-fx-background-color: white");
		//glückPane.setPadding(rand);
		
		return glückwunschSzene;
	}

}
