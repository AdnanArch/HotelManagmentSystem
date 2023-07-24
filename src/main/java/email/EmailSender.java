package email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {

    public boolean sendEmail(String to, String from, String subject, String text) {
        boolean flag = false;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");

        //set your own SMTP server properties and your own username and password here.
        // TODO: Write your own username of email from which you want to send the email
        // For example if i want to send emails from something@gmail.com then something will be my username.

        String username = "your_email_username_from_which_you_want_to_send_emails";

        // TODO: Write 16 digit password here which you can generate by going into manage your google account
        // then head over to security option and turn on 2-step verification and after turning it on you can
        // see an option of App Passwords click on it and generate a password of 16 characters and paste that password
        // here.
        String password = "16_digit_password";

        //session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return flag;
    }

}

