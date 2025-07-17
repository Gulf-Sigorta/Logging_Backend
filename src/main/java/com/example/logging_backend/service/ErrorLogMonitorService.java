package com.example.logging_backend.service;

import com.example.logging_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ErrorLogMonitorService {

    private static final double ERROR_THRESHOLD_PERCENT = 20.0; // %20 eşik
    private static final double NORMAL_PERCENT = 10.0; // normalde %10 kabul ediliyor

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private EmailService emailService;

    private boolean alertSent = false;

    @Scheduled(fixedRate = 60000) // 10 dakikada bir çalışır
    public void checkErrorLogRatio() {
        Timestamp tenMinutesAgo = Timestamp.from(Instant.now().minus(10, ChronoUnit.MINUTES));

        long totalLogs = logRepository.countAllLogsSince(tenMinutesAgo);
        long errorLogs = logRepository.countErrorLogsSince(tenMinutesAgo);

        System.out.println("🔄 ErrorLogMonitorService çalıştı. Toplam log: " + totalLogs + ", Error log: " + errorLogs);

        try {
            System.out.println("📧 Mail gönderiliyor...");
            emailService.sendSimpleEmail("canakduruk@gmail.com","Başlık","İçerik");
            System.out.println("✅ Mail gönderildi.");
        } catch (MailException e) {
            System.err.println("❌ Mail gönderilemedi: " + e.getMessage());
            e.printStackTrace();
        }

        if (totalLogs == 0) return; // Bölme hatasından kaçınmak için

        double errorPercentage = ((double) errorLogs / totalLogs) * 100;

        if (true) {
            String message = String.format(
                    "Son 10 dakikadaki toplam log: %d\nERROR log sayısı: %d\nHata oranı: %.2f%%\n\nBu oran %d%% normal seviyesinin üzerindedir!",
                    totalLogs, errorLogs, errorPercentage, (int) NORMAL_PERCENT
            );

            try {
                System.out.println("📧 Mail gönderiliyor...");
                emailService.sendSimpleEmail("canakduruk@gmail.com","Başlık","İçerik");
                System.out.println("✅ Mail gönderildi.");
            } catch (MailException e) {
                System.err.println("❌ Mail gönderilemedi: " + e.getMessage());
                e.printStackTrace();
            }


            alertSent = true;
        } else if (errorPercentage < ERROR_THRESHOLD_PERCENT) {
            alertSent = false; // oran düşerse tekrar mail atılabilir hale gelsin
        }
    }
}
