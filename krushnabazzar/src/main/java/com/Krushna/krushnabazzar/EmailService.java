package com.Krushna.krushnabazzar;

import java.io.File;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public boolean sendEmail(String subject,String message,String to){
//      rest of the code
        boolean f = false;
        String from = "krushnaesuperbazzar25@gmail.com";  //make new email here for krushnabazzar

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        //step-1 create session
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("krushnaesuperbazzar25@gmail.com", "tjsi xarl vyaf xfgi"); // replace the password for krushna email
            }
        });

        session.setDebug(true);

        try {
            //      	step-2 compose session
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(from));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);
            m.setContent(message, "text/html; charset=utf-8");
            //step-3 sending the mail
            Transport.send(m);
            System.out.println("Email sent successfully.");
            f = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
