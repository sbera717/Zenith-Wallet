package com.example.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailUser {
    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSenderImpl javaMailSender;
    public void sendEmail(String incEmail, String incBody, String subject) throws MessagingException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(incEmail);
        helper.setSubject(subject);
        helper.setFrom("Zenith <koulik.saha14@gmail.com>");
        helper.setText(incBody, false);

        javaMailSender.send(mimeMessage);
    }
}
