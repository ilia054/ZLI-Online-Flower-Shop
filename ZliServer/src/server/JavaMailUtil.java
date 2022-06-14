package server;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {
	public static void sendMail(String recepient,String order) throws Exception {

		Properties properties=new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		String myAccountEmail="zlionlineflowershop@gmail.com";
		String password="";
		Session session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail, password);
			}
		});
		
		Message message=prepareMessage(session,myAccountEmail,recepient,order);
		Transport.send(message);
		System.out.println("Message Sent successfully");
	}

	private static Message prepareMessage(Session session,String from,String to,String orderInfo) {
		Message message = new MimeMessage(session);
		try {
			
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("New Message from Zli Online Flower Shop");
			message.setText(orderInfo);
			return message;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}