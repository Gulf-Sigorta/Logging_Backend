package com.example.logging_backend.service;

import com.example.logging_backend.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LogMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(LogMonitorService.class);

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

    // 1. Log bilgilerini getir
    private LogStats fetchLogStats() {
        Timestamp tenMinutesAgo = Timestamp.from(Instant.now().minus(10, ChronoUnit.MINUTES));
        long totalLogs = logRepository.countAllLogsSince(tenMinutesAgo);
        long errorLogs = logRepository.countErrorLogsSince(tenMinutesAgo);
        return new LogStats(totalLogs, errorLogs);
    }

    // 2. Email şablonunu yükle
    private String loadTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/error-email-template.html");
        return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
    }

    // 3. Email içeriğini hazırla
    private String prepareEmailContent(String template, int totalLogs, int errorLogs, double errorPercentage) {
        return template
                .replace("${totalLogs}", String.valueOf(totalLogs))
                .replace("${errorLogs}", String.valueOf(errorLogs))
                .replace("${errorPercentage}", String.format("%.2f", errorPercentage))
                .replace("${normalPercent}", String.valueOf((int) NORMAL_PERCENT));
    }

    // 4. Email gönder
    private void sendEmail(List<String> recipients, String subject, String htmlMessage) {
        logger.info("📧 Mail gönderiliyor...");
        emailService.sendSimpleEmail(recipients, subject, htmlMessage);
        logger.info("✅ Mail gönderildi.");
    }

    // 5. Push bildirimi gönder
    private void sendPushNotification(double errorPercentage) {
        fcmService.sendPushNotificationToTopic(
                "log",
                "⚠️ Yüksek Hata Oranı",
                String.format("Son 10 dakikada hata oranı %.2f%% seviyesinde!", errorPercentage)
        );
    }

    // 6. Hata oranı kontrolü ve işlem tetiklemesi
    @Scheduled(fixedRate = 60000) // 1 dakikada bir çalışır
    public void checkErrorLogRatio() {
        LogStats stats = fetchLogStats();

        logger.info("🔄 ErrorLogMonitorService çalıştı. Toplam log: {}, Error log: {}", stats.totalLogs, stats.errorLogs);

        if (stats.totalLogs == 0) return; // Bölme hatasından kaçınmak için

        double errorPercentage = ((double) stats.errorLogs / stats.totalLogs) * 100;

        if (errorPercentage >= ERROR_THRESHOLD_PERCENT && !alertSent) {
            String subject = String.format("⚠️ Yüksek Hata Oranı: Son 10 dakikada %.2f%% ERROR Log", errorPercentage);
            try {
                logger.info("📧 Mail şablonu yükleniyor...");
                String template = loadTemplate();
                String htmlMessage = prepareEmailContent(template, (int) stats.totalLogs, (int) stats.errorLogs, errorPercentage);

                List<String> recipients = authService.getAllUsersEmails();
                logger.info("Alıcılar: {}", recipients);

                sendEmail(recipients, subject, htmlMessage);
                sendPushNotification(errorPercentage);

                alertSent = true;

            } catch (IOException e) {
                logger.error("❌ Mail şablonu yüklenirken hata: {}", e.getMessage(), e);
            } catch (MailException e) {
                logger.error("❌ Mail gönderilemedi: {}", e.getMessage(), e);
            }
        } else if (errorPercentage < ERROR_THRESHOLD_PERCENT) {
            alertSent = false; // oran düştü, yeniden uyarı atılabilir
        }
    }

    // Yardımcı sınıf: log sayıları taşımak için
        private record LogStats(long totalLogs, long errorLogs) {
    }
}
