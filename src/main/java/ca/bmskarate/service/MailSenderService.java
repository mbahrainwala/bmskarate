package ca.bmskarate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender sender;

    public void sendMail(String subject, String email) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo("mbahrainwala@hotmail.com");
        helper.setText("How are you?");
        helper.setSubject("Hi");
        helper.setFrom("mbahrainwala@hotmail.com");

        sender.send(message);
    }
}
