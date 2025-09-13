package br.com.codekillers.zelo.Utils;

import com.google.cloud.Timestamp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Date {
    public static boolean isToday(Timestamp date) {
        if (date == null) {
            return false;
        }

        LocalDate dataCheckIn = date.toSqlTimestamp().toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();

        return dataCheckIn.isEqual(today);
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd 'de' MMMM - HH':'mm")
                    .withZone(ZoneId.of("America/Sao_Paulo"));

    public static String formatFirestoreTimestamp(Timestamp ts) {
        return FORMATTER.format(ts.toSqlTimestamp().toInstant());
    }
}
