package it.mozzicato.mercatino;

import it.infracom.jwolf.JWApplication;
import it.infracom.jwolf.connection.NullManager;
import it.infracom.jwolf.service.AbstractService;
import it.infracom.jwolf.utils.StringUtils;
import it.mozzicato.mercatino.server.MercatinoConfiguration;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
 
public class MailService extends AbstractService<NullManager> {
    private static Properties mailServerProperties;
 
    public static void setMailServerProperties(Properties mailServerProperties) {
		MailService.mailServerProperties = mailServerProperties;
	}
    
    public void sendEmail(String subject, String message, String... recipients) {
    	logger.info("----------- INVIO MAIL " + subject + "\n" + message);
//        System.out.println("\n\n 2nd ===> get Mail Session..");
        try {
			Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			MimeMessage generateMailMessage = new MimeMessage(getMailSession);
			for (String recipient : recipients) {
				generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			}
			String from = mailServerProperties.getProperty("mail.from");
			generateMailMessage.setFrom(new InternetAddress(from));
			generateMailMessage.setSubject(subject);
			generateMailMessage.setContent(message, "text/html");
//        System.out.println("Mail Session has been created successfully..");
 
//        System.out.println("\n\n 3rd ===> Get Session and Send mail");
			Transport transport = getMailSession.getTransport("smtp");

			String server = mailServerProperties.getProperty("mail.server");
			String user = mailServerProperties.getProperty("mail.user");
			String password = mailServerProperties.getProperty("mail.password");
			transport.connect(server, user, password);
			
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
		}
		catch (Exception e) {
			logger.error("Errore durante l'invio della mail a: " + Arrays.toString(recipients) + "\n" + 
				StringUtils.stackTraceToString(e));
		}
    }
    
    public static void main(String[] args) throws AddressException, MessagingException {
    	JWApplication.init(new MercatinoConfiguration());
//    	Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider()); 
		MailService mailService = JWApplication.getService(MercatinoConfiguration.SERVIZIO_MAIL);

		mailService.sendEmail("ciao", "ciccio", "bitblasters@gmail.com");
	}
}