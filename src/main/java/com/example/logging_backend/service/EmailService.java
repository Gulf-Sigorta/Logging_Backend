package com.example.logging_backend.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Basit bir e-posta gönderir.
     * @param toEmail Alıcının e-posta adresi.
     * @param subject E-postanın konusu.
     * @param body E-postanın içeriği.
     */
    public void sendSimpleEmail(String toEmail, String subject, String body) { // Parametreler eklendi
        SimpleMailMessage message = new SimpleMailMessage();
        // Gönderen mail adresi. application.properties'deki username ile aynı olması önerilir.
        message.setFrom("Caner.Akduruk@gig.com.tr");
        message.setTo("Caner.Akduruk@gig.com.tr"); // Dinamik alıcı
        message.setSubject(subject); // Dinamik konu
        message.setText(body); // Dinamik içerik
        mailSender.send(message);
        System.out.println("Mail gönderildi: " + toEmail + " konusuna: '" + subject + "'");
    }
}