package com.example.logging_backend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendSimpleEmail(List<String> toAddresses, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom("Eray.Duman@gig.com.tr");
            helper.setTo(toAddresses.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true); // İkinci parametre true olursa HTML olarak gönderir

            mailSender.send(mimeMessage);
            System.out.println("HTML mail gönderildi: " + toAddresses + " konusuna: '" + subject + "'");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Mail gönderme hatası: " + e.getMessage());
        }
    }
}