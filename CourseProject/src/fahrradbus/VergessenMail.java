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
 * verschickt die Passwort-Vergessen-Mail mit der User-Mailadresse und dem User-Passwort
 * @author Charlin
 *
 */
public class VergessenMail {

	String msg = " ";
	String subject = "Dein Passwort";
	String userEmail;
	String userPassword;
	String host = "mail.gmx.net";
	String password = "zahlenschloss123";
	String email = "fahrradbus@gmx.de";
	Properties props = new Properties();

	/**
	 * der Constructor
	 * @param ue User-EMail
	 * @param up User-Passwort
	 * @param s Betreff der Mail
	 */
	public VergessenMail(String ue, String up, String s) {
		subject = s;
		userEmail = ue;
		userPassword = up;
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
	 * Versendet die Passwort-Vergessen-Mail
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
			messageBodyPart
					.setText(msg + " Das Passwort zu deiner E-Mail-Adresse " + userEmail + " lautet: " + userPassword);
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
