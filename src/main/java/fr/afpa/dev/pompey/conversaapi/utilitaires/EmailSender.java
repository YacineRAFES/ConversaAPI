package fr.afpa.dev.pompey.conversaapi.utilitaires;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.*;

public class EmailSender {
//
//    private static final Properties PROPERTIES = new Properties();
//    private static final String USERNAME = "admin@gmail.in";   //change it
//    private static final String PASSWORD = "password";   //change it
//    private static final String HOST = "smtp.gmail.com";
//
//    static {
//        PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
//        PROPERTIES.put("mail.smtp.port", "587");
//        PROPERTIES.put("mail.smtp.auth", "true");
//        PROPERTIES.put("mail.smtp.starttls.enable", "true");
//    }
//
//    public static void sendPlainTextEmail(String from, String to, String subject, List<String> messages, boolean debug) {
//
//        Authenticator authenticator = new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(USERNAME, PASSWORD);
//            }
//        };
//
//        Session session = Session.getInstance(PROPERTIES, authenticator);
//        session.setDebug(debug);
//
//        try {
//
//            // créer un message avec l'en-tête
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(from));
//            InternetAddress[] address = {new InternetAddress(to)};
//            msg.setRecipients(Message.RecipientType.TO, address);
//            msg.setSubject(subject);
//            msg.setSentDate(new Date());
//
//            // créer le corps du message
//            Multipart mp = new MimeMultipart();
//            for (String message : messages) {
//                MimeBodyPart mbp = new MimeBodyPart();
//                mbp.setText(message, "us-ascii");
//                mp.addBodyPart(mbp);
//            }
//            msg.setContent(mp);
//
//            // Envoye le message
//            Transport.send(msg);
//
//        } catch (MessagingException mex) {
//            mex.printStackTrace();
//            Exception ex = null;
//            if ((ex = mex.getNextException()) != null) {
//                ex.printStackTrace();
//            }
//        }
//    }
}
