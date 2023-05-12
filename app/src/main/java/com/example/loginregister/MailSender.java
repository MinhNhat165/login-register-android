package com.example.loginregister;

import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends AsyncTask<Void, Void, Void> {
        private String recipientEmail;
        private String messageBody;

        public MailSender(String recipientEmail, String messageBody) {
            this.recipientEmail = recipientEmail;
            this.messageBody = messageBody;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String from = "devemail301101@gmail.com";
                String password = "ovzvfjsndwatxfoy";
                // Set up properties for the email server
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Authenticate with the email server
                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

                // Compose the email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Your OTP");
                message.setText(messageBody);

                // Send the email message
                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

