package ca.bmskarate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    Environment env;

    public void sendMail(String emailAddress, String subject, String emailText) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(emailAddress);
        helper.setText(emailText);
        helper.setSubject(subject);
        helper.setFrom(env.getProperty("mail.password.from"));

        sender.send(message);
    }
}
