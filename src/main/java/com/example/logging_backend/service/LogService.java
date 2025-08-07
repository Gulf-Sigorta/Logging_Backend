package com.example.logging_backend.service;

import com.example.logging_backend.model.HourlyLogCount;
import com.example.logging_backend.model.Log.Log;
import com.example.logging_backend.model.Log.LogLevelCount;
import com.example.logging_backend.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Page<Log> getAllLogs(Pageable pageable) {
        try {
            return logRepository.findAllByOrderByTimestampDesc(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Tüm loglar alınırken hata oluştu", e);
        }
    }

    public Page<Log> getLogsByLevel(String level, Pageable pageable) {
        try {
            return logRepository.findByLevelOrderByTimestampDesc(level, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Loglar seviyeye göre getirilirken hata oluştu", e);
        }
    }

    public List<LogLevelCount> getLogCountsByLevel() {
        try {
            return logRepository.countLogsByLevel();
        } catch (Exception e) {
            throw new RuntimeException("Seviyelere göre log sayıları alınırken hata oluştu", e);
        }
    }

    public List<LogLevelCount> getTodayLogCountsByLevel() {
        try {
            // "Bugün" kavramını İstanbul zaman dilimine göre hesapla
            ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");
            ZonedDateTime nowInIstanbul = ZonedDateTime.now(istanbulZone);
            LocalDate todayInIstanbul = nowInIstanbul.toLocalDate();

            LocalDateTime startOfDayIstanbul = todayInIstanbul.atStartOfDay();
            LocalDateTime endOfDayIstanbul = todayInIstanbul.plusDays(1).atStartOfDay();

            // Veritabanı UTC olarak sakladığı varsayılarak, İstanbul saatlerini UTC'ye dönüştür
            LocalDateTime startOfDayUtc = startOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endOfDayUtc = endOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

            // Not: countTodayLogsByLevel metodu hala CURRENT_DATE kullanıyor.
            // Eğer bu metot da zaman dilimi sorununa neden oluyorsa,
            // LogRepository'ye yeni bir metot ekleyip startOfDayUtc ve endOfDayUtc'yi parametre olarak geçmelisiniz.
            return logRepository.countTodayLogsByLevel();
        } catch (Exception e) {
            throw new RuntimeException("Bugünün log sayıları alınırken hata oluştu", e);
        }
    }

    public List<LogLevelCount> getLogCountsByLevelFromDate(LocalDate startDate) {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);

            return logRepository.countLogsByLevelFromDate(startDateTime, endDateTime);
        } catch (Exception e) {
            throw new RuntimeException("Belirtilen tarihten itibaren log sayıları alınırken hata oluştu", e);
        }
    }

    public List<Log> getLogsFromToday() {
        try {
            // "Bugün" kavramını İstanbul zaman dilimine göre hesapla
            ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");
            ZonedDateTime nowInIstanbul = ZonedDateTime.now(istanbulZone);
            LocalDate todayInIstanbul = nowInIstanbul.toLocalDate();

            LocalDateTime startOfDayIstanbul = todayInIstanbul.atStartOfDay();
            LocalDateTime endOfDayIstanbul = todayInIstanbul.plusDays(1).atStartOfDay();

            // Veritabanı UTC olarak sakladığı varsayılarak, İstanbul saatlerini UTC'ye dönüştür
            LocalDateTime startOfDayUtc = startOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            // Not: findAllTodayLogs metodu hala CURRENT_DATE kullanıyor.
            // Eğer bu metot da zaman dilimi sorununa neden oluyorsa,
            // LogRepository'ye yeni bir metot ekleyip startOfDayUtc ve endOfDayUtc'yi parametre olarak geçmelisiniz.
            return logRepository.findAllTodayLogs();
        } catch (Exception e) {
            throw new RuntimeException("Bugünün logları alınırken hata oluştu", e);
        }
    }

    // YENİ: Belirli bir tarihe göre logları getirme metodu
    // Bu metot, frontend'den gelen tarihi İstanbul zaman diliminde yorumlayıp UTC'ye dönüştürür.
    public Page<Log> getLogsByDate(LocalDate date, Pageable pageable) {
        try {
            ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");

            // Frontend'den gelen LocalDate'i İstanbul zaman diliminde gün başlangıcı ve sonu olarak al
            LocalDateTime startOfDayIstanbul = date.atStartOfDay();
            LocalDateTime endOfDayIstanbul = date.plusDays(1).atStartOfDay().minusNanos(1);

            // Bu İstanbul zaman dilimi değerlerini UTC'ye dönüştürerek veritabanı sorgusu için hazırla
            // (Veritabanının zaman damgalarını UTC olarak sakladığı varsayılır)
            LocalDateTime startOfDayUtc = startOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endOfDayUtc = endOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

            return logRepository.findByTimestampBetweenOrderByTimestampDesc(startOfDayUtc, endOfDayUtc, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Belirtilen tarihe göre loglar alınırken hata oluştu", e);
        }
    }

    // YENİ: Belirli bir tarih ve seviyeye göre logları getirme metodu
    // Bu metot, frontend'den gelen tarihi İstanbul zaman diliminde yorumlayıp UTC'ye dönüştürür.
    public Page<Log> getLogsByDateAndLevel(LocalDate date, String level, Pageable pageable) {
        try {
            ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");

            // Frontend'den gelen LocalDate'i İstanbul zaman diliminde gün başlangıcı ve sonu olarak al
            LocalDateTime startOfDayIstanbul = date.atStartOfDay();
            LocalDateTime endOfDayIstanbul = date.plusDays(1).atStartOfDay().minusNanos(1);

            // Bu İstanbul zaman dilimi değerlerini UTC'ye dönüştürerek veritabanı sorgusu için hazırla
            // (Veritabanının zaman damgalarını UTC olarak sakladığı varsayılır)
            LocalDateTime startOfDayUtc = startOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endOfDayUtc = endOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

            return logRepository.findByTimestampBetweenAndLevelOrderByTimestampDesc(startOfDayUtc, endOfDayUtc, level, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Belirtilen tarih ve seviyeye göre loglar alınırken hata oluştu", e);
        }
    }

    public List<HourlyLogCount> getHourlyLogCountsForToday() {
        // 1. İstanbul zaman dilimini tanımla
        ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");

        // 2. İstanbul'a göre bugünün tarihini al
        ZonedDateTime nowInIstanbul = ZonedDateTime.now(istanbulZone);
        LocalDate todayInIstanbul = nowInIstanbul.toLocalDate();

        // 3. İstanbul'a göre bugünün başlangıç ve bitiş saatlerini (LocalDateTime olarak) hesapla
        LocalDateTime startOfDayIstanbul = todayInIstanbul.atStartOfDay();
        LocalDateTime endOfDayIstanbul = todayInIstanbul.plusDays(1).atStartOfDay(); // Bitiş saati (hariç)

        // 4. Bu zamanları UTC'ye dönüştürerek veritabanı sorgusu için hazırla
        // (Veritabanının zaman damgalarını UTC olarak sakladığı varsayılır)
        LocalDateTime startOfDayUtc = startOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endOfDayUtc = endOfDayIstanbul.atZone(istanbulZone).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        // 5. Repository'den saatlik log sayılarını çek
        List<Object[]> results = logRepository.countLogsByHour(startOfDayUtc, endOfDayUtc);

        // 6. Tüm 24 saati başlangıçta 0 sayısıyla bir haritaya doldur
        Map<Integer, Long> hourlyMap = IntStream.range(0, 24)
                .boxed()
                .collect(Collectors.toMap(
                        hour -> hour,
                        hour -> 0L
                ));

        // 7. Veritabanından gelen sonuçları işle ve İstanbul saatine dönüştür
        for (Object[] row : results) {
            int utcHour = ((Integer) row[0]); // Veritabanından gelen saat (UTC olduğu varsayılır)
            long count = (Long) row[1];

            // UTC saatini İstanbul saatine dönüştür (İstanbul UTC+3)
            int istanbulHour = (utcHour + 3) % 24;
            if (istanbulHour < 0) { // Modulo negatif sonuç verirse düzelt
                istanbulHour += 24;
            }

            hourlyMap.put(istanbulHour, count);
        }

        // 8. Haritayı sıralı bir HourlyLogCount listesine dönüştür
        List<HourlyLogCount> hourlyCounts = hourlyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new HourlyLogCount(String.format("%02d:00", entry.getKey()), entry.getValue()))
                .collect(Collectors.toList());

        return hourlyCounts;
    }
}
