package fahrradbus;
/**
 * Stellt einen Bus mit Verfügbarkeitszeitraum dar.
 * @author Charlin
 *
 */
public class Bus {
	int nummer;
	String zeitGeblockt;

	public Bus(int n, String geblockt) {
		nummer = n;
		zeitGeblockt = geblockt;
	}
	
}
