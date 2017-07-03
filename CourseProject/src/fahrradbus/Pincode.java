package fahrradbus;

import java.security.SecureRandom;

public class Pincode {

	public String codeNeu;
	private String[] pinsDiesesJahr;
	private String[] pinsNächstesJahr;
	
	public String getPincode(int jahr, int kalenderwoche) {
		if (jahr == 0) {
			return pinsDiesesJahr[kalenderwoche - 1];
		}
		return pinsNächstesJahr[kalenderwoche - 1];
	}
	
	public void nächstesJahr() {
		pinsDiesesJahr = pinsNächstesJahr;
		initJahr(1);
	}

	public String createNewPin(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder pass = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			pass.append(random.nextInt(10));

		}
		return pass.toString();
	}
	
	public void init() {
		initJahr(0);
		initJahr(1);
	}
	
	public void setPins(int j, String[] p) {
		if (j == 0) {
			pinsDiesesJahr = p;
		} else {
			pinsNächstesJahr = p;
		}
	}
	
	private void initJahr(int j) {
		String[] pins;
		if (j == 0) {
			pinsDiesesJahr = new String[52];
			pins = pinsDiesesJahr;
		} else {
			pinsNächstesJahr = new String[52];
			pins = pinsNächstesJahr;
		}
		for (int i = 0; i < 52; i++) {
			pins[i] = createNewPin(4);
		}
	}
	
}
