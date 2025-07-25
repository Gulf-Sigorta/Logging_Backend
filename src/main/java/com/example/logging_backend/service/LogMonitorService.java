package com.example.logging_backend.service;

import com.example.logging_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LogMonitorService {

    private static final double ERROR_THRESHOLD_PERCENT = 20.0; // %20 eşik
    private static final double NORMAL_PERCENT = 10.0; // normalde %10 kabul ediliyor

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private AuthService authService;

    private boolean alertSent = false;

    @Scheduled(fixedRate = 60000) // 10 dakikada bir çalışır
    public void checkErrorLogRatio() {
        Timestamp tenMinutesAgo = Timestamp.from(Instant.now().minus(10, ChronoUnit.MINUTES));

        long totalLogs = logRepository.countAllLogsSince(tenMinutesAgo);
        long errorLogs = logRepository.countErrorLogsSince(tenMinutesAgo);

        System.out.println("🔄 ErrorLogMonitorService çalıştı. Toplam log: " + totalLogs + ", Error log: " + errorLogs);

        if (totalLogs == 0) return; // Bölme hatasından kaçınmak için

        double errorPercentage = ((double) errorLogs / totalLogs) * 100;

        if (errorPercentage >= ERROR_THRESHOLD_PERCENT && !alertSent) {

            String subject = String.format(
                    "⚠️ Yüksek Hata Oranı: Son 10 dakikada %.2f%% ERROR Log", errorPercentage
            );
            String message = String.format(
                    "📊 Son 10 dakikadaki toplam log: %d\n" +
                            "❌ ERROR log sayısı: %d\n" +
                            "⚠️ Hata oranı: %.2f%%\n\n" +
                            "🚨 Bu oran %d%% normal seviyesinin üzerindedir!",
                    totalLogs, errorLogs, errorPercentage, (int) NORMAL_PERCENT
            );

            String htmlMessage = String.format(
                    "<div style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " +
                            "background: #f4f7fa; padding: 20px; border-radius: 10px; max-width: 500px;\">" +
                            "<h2 style=\"color: #2c3e50;\">⚠️ Hata Oranı Uyarısı</h2>" +
                            "<p style=\"font-size: 16px; color: #34495e;\">Son 10 dakikadaki log bilgileri:</p>" +
                            "<ul style=\"list-style: none; padding: 0; font-size: 16px; color: #34495e;\">" +
                            "<li>📊 <strong>Toplam Log:</strong> <span style=\"color: #2980b9; font-weight: bold;\">%d</span></li>" +
                            "<li>❗ <strong>ERROR Log Sayısı:</strong> <span style=\"color: #e74c3c; font-weight: bold;\">%d</span></li>" +
                            "<li>📈 <strong>Hata Oranı:</strong> <span style=\"color: #c0392b; font-weight: bold;\">%.2f%%</span></li>" +
                            "</ul>" +
                            "<p style=\"background: #e74c3c; color: white; padding: 10px; border-radius: 5px; " +
                            "font-weight: bold; text-align: center; max-width: 400px; margin: 20px auto 0;\">" +
                            "Bu oran %d%% normal seviyesinin üzerindedir!</p>" +
                            "</div>",
                    totalLogs, errorLogs, errorPercentage, (int) NORMAL_PERCENT
            );





            try {
                System.out.println("📧 Mail gönderiliyor...");
                List<String> recipients =  authService.getAllUsersEmails();
                System.out.println(recipients);
                emailService.sendSimpleEmail(recipients, subject, htmlMessage);
                System.out.println("✅ Mail gönderildi.");
                fcmService.sendPushNotificationToTopic(
                        "log",
                        "⚠️ Yüksek Hata Oranı",
                        String.format("Son 10 dakikada hata oranı %.2f%% seviyesinde!", errorPercentage)
                );

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
