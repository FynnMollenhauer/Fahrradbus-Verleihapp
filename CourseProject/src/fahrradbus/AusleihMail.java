package fahrradbus;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * verschickt die Buchungsbestätigungsmail mit Pincodes, Daten, Busnummern und weiteren Hinweisen.
 * @author Charlin
 *
 */
public class AusleihMail {
	String msg1 = "Guten Tag,\n\n"
			+ "Deine Buchung bei fahrradbus war erfolgreich.\n"
			+ "Der Bus kann am Entleih-Tag ab 10 Uhr morgens Am Sande 1 abgeholt werden und das Zahlenschloss kann mit dem untenstehenden Code geöffnet werden.\n\n"
			+ "Am Rückgabe-Tag muss der Fahrradbus bis 22 Uhr an den Abholort gebracht werden und mit dem zweiten, unten genannten Zahlenschloss-Code gesichert werden. "
			+ "Im Falle eines technischen Notfalls erreichst du uns unter folgender Handynummer: 0176/30391523.\n\n";
	String msg2 = "\n\nWir wünschen dir viel Spaß mit dem Fahrradbus und danken für dein Vertrauen!\n\n"
			+ "Der Fahrradbus steht dir kostenfrei zur Verfügung, allerdings freuen wir uns immer über Spenden für Instandhaltungskosten auf das Konto des\n"
			+ "Sozialkraftwerk e.V.\n"
			+ "IBAN: DE13430609677037333900\n"
			+ "BIC: GENODEM1GLS\n"
			+ "BANK: GLS Gemeinschaftsbank\n\n"
			+ "Dein Fahrradbus-Team";
	String code = "";
	String ausleihTage = "";
	String subject = "Deine Buchung bei Fahrradbus";
	String host = "mail.gmx.net";
	String password = "zahlenschloss123";
	String email = "fahrradbus@gmx.de";
	String busNummern = "";
	String tage = "";
	Properties props = new Properties();

	/**
	 * der Constructor (Methode, die aufgerufen wird, wenn ein neues Objekt der Klasse AusleihMail erzeugt wird)
	 * @param c der fertig formatierte Text mit den generierten Pincodes 
	 * @param tage die Ausleihtage
	 */
	public AusleihMail(String c, String tage) {
		code = c;
		ausleihTage = tage;
		
		//die Einstellungen für den Mailservice
		Properties props = System.getProperties();
		props.setProperty("mail.smtps.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "587");
		props.setProperty("mail.smtp.socketFactory.port", "587");
		props.setProperty("mail.smtps.auth", "true");
	}

	/**
	 * Versendet die Ausleih-Mail
	 * @param address
	 * @return true wenn die Mail erfolgreich versendet werden konnte
	 */
	public boolean sendMail(String address) {
		//Sammelt die Einstellungen
		Session session = Session.getInstance(props, null);

		//Multipurpose Internet Mail Extensions
		//Definiert das Datenformat der E-Mail
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(email));
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject(subject);
		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		//Bodypart enthält den Inhalt der MimeMessage
		BodyPart messageBodyPart = new MimeBodyPart();
		try {
			//Zusammenfügen der Nachricht der E-Mail
			messageBodyPart.setText(msg1 + " " + code + "Gebuchte Tage: " + ausleihTage + "\n" + msg2);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		//Versenden der E-Mail
		try {
			Transport transport = session.getTransport("smtps");
			transport.connect(host, email, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (SendFailedException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}