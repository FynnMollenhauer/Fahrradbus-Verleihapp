package fahrradbus;

import java.util.List;
import java.time.LocalDate;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * erstellt die Kalender-Szene und integriert die geblockten Tage
 * aus der BusBank.
 * @author Charlin
 *
 */
public class Calendar {
  
	private List<Bus> busse;
 
    private DatePicker entleihenDatePicker;
    private DatePicker rückgabeDatePicker;
    private ComboBox<String> anzahlPlätze;
    private Button buchen = new Button("Verbindlich buchen");
    
    public Calendar(List<Bus> busse) {
    	this.busse = busse;
    }
  
    /**
     * stellt die Szene Calendar zusammen.
     * @return die Calendar-Szene
     */
    public Scene getScene() {
    	//Infos zur Zeitzone
	    Locale.setDefault(Locale.GERMANY); 
	    
        VBox vbox = new VBox(20);
        vbox.setStyle("-fx-padding: 10;");
        Scene calendarScene = new Scene(vbox, 400, 400);
        entleihenDatePicker = new DatePicker();
        
        calendarScene.getStylesheets().add("file:fahrradbus.css");
        
        //verhindert, dass der Nutzer das Textfeld des DatePickers bearbeiten kann
        entleihenDatePicker.setEditable(false);
        rückgabeDatePicker = new DatePicker();
        rückgabeDatePicker.setEditable(false);
        
		buchen.setPrefSize(150, 40);
		//deaktiviert den Buchen-Button
		buchen.setDisable(true);
        
		//wird das Entleihdatum nach dem Rückgabedatum geändert, wird das Rückgabedatum wieder zurück gesetzt
        entleihenDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
        	rückgabeDatePicker.setValue(null);
        });
        
    	//Buchen-Button wird aktiviert, wenn der Rückgabe-Datepicker einen Wert erhält.
        rückgabeDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
        	buchen.setDisable(false);
        });
        
       
        entleihenDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // deaktiviere die Auswahl für alle Daten, die vor dem aktuellen Datum liegen
                setDisable(empty || date.isBefore(LocalDate.now()) || isDateBlocked(date));
            }
        });
        
        //legt fest, welche Zellen des rückgabeDatePickers aktiv sind und welche nicht
        rückgabeDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // aktiviere den RÜckgabe-Datepicker erst, wenn das Entleihdatum angegeben wurde.
                if (entleihenDatePicker.getValue() == null) {
                	setDisable(true);
                } else { //blockiere die Tage vor dem Entleihdatum, gebuchte Tage und alle, die mehr als 2 Tage nach dem Entleihdatum liegen.
	                setDisable(empty || date.isBefore(entleihenDatePicker.getValue()) || 
	                isDateBlocked(date) || date.isAfter(entleihenDatePicker.getValue().plusDays(2)));
                }
            }
        });
        
        //Array mit den Werten der Combobox anlegen
        ObservableList<String> anzahlPlätzeList = 
        	    FXCollections.observableArrayList(
        	        "2",
        	        "4",
        	        "6"
        	    );
        //Array anzahlPlätzeList in die ComboBox einspeisen
        anzahlPlätze = new ComboBox<String>(anzahlPlätzeList);
        anzahlPlätze.getSelectionModel().selectFirst();
        //setze die Datepicker zurück, wenn die Anzahl der Plätze geändert wird und deaktiviere den buchen-Button
        anzahlPlätze.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
            	   entleihenDatePicker.setValue(null);
            	   rückgabeDatePicker.setValue(null);
            	   buchen.setDisable(true);
              }    
          });
        
        GridPane gridPane = new GridPane();
        Label plätzeLabel = new Label("Gewünschte Anzahl an Plätzen:");
        gridPane.add(plätzeLabel, 0, 0);
        gridPane.add(anzahlPlätze, 0, 1);
        Label entleihlabel = new Label("Entleihdatum:");
        gridPane.add(entleihlabel, 0, 2);
        GridPane.setHalignment(entleihlabel, HPos.LEFT);
        gridPane.add(entleihenDatePicker, 0, 3);
        Label rückgabelabel = new Label("Rückgabedatum:");
        gridPane.add(rückgabelabel, 0, 4);
        GridPane.setHalignment(rückgabelabel, HPos.LEFT);
        gridPane.add(rückgabeDatePicker, 0, 5);
        gridPane.add(buchen, 0, 6);
        vbox.getChildren().add(gridPane);
        
        return calendarScene;
    }
    
    /**
     * Prüfe, ob genug Plätze (ausgewählt aus der ComboBox)
     * für date verfügbar sind.
     * @param date - Datumswert
     * @return ist das abgefragte Datum geblockt? 
     */
    public boolean isDateBlocked(LocalDate date) {
    	String[] dates;
    	int freiePlätze = 6;
    	//für jeden Bus aus der Liste Busse
    	for (Bus bus : busse) {
    		dates = bus.zeitGeblockt.split(":");
    		if (!(dates.length == 1 && dates[0].equals(""))) {
    			for (int i = 0; i < dates.length; i++) {
    				if (LocalDate.parse(dates[i]).equals(date)) {
    					freiePlätze -= 2;
    				}
    			}
    		}
    	}
    	// gib "nicht geblockt" zurück, wenn die Anzahl der freien Plätze größer/gleich als die Anzahl der ausgewählten Plätze (Combobox) ist.
    	if (freiePlätze >= Integer.parseInt(anzahlPlätze.getValue())) {
    		return false;
    	}
    	return true;
    }
    
    public void setBuchenEventHandler(EventHandler<ActionEvent> buchenHandler) {
		buchen.setOnAction(buchenHandler);
	}

	public int getAnzahlPlätze() {
		return Integer.parseInt(anzahlPlätze.getValue());
	}

	public String getDates() {
		String dates = entleihenDatePicker.getValue().toString();
		LocalDate datum = entleihenDatePicker.getValue();
		while (!datum.equals(rückgabeDatePicker.getValue())) {
			datum = datum.plusDays(1);
			dates = dates + ":" + datum.toString();
		}
		return dates;
	}
    
    
    
}
