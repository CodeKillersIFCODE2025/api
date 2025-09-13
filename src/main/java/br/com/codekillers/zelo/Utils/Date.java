package br.com.codekillers.zelo.Utils;

import br.com.codekillers.zelo.Domain.FrequencyUnit;
import com.google.cloud.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Date {
    public static boolean isToday(Timestamp date) {
        if (date == null) {
            return false;
        }

        LocalDate dataCheckIn = toLocalDateTime(date).toLocalDate();
        LocalDate today = LocalDate.now();

        return dataCheckIn.isEqual(today);
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd 'de' MMMM - HH':'mm")
                    .withZone(ZoneId.of("America/Sao_Paulo"));

    public static String formatFirestoreTimestamp(Timestamp ts) {
        return FORMATTER.format(ts.toSqlTimestamp().toInstant());
    }

    public static LocalDateTime toLocalDateTime(Timestamp date) {
        return date.toSqlTimestamp().toLocalDateTime();
    }

    public static Timestamp toTimestamp(LocalDateTime date) {
        return Timestamp.of(java.sql.Timestamp.valueOf(date));
    }

    public static Timestamp calculateNextDate(Timestamp timestamp, FrequencyUnit unit){
        LocalDateTime date = toLocalDateTime(timestamp);

        switch (unit){
            case DAILY:
                date = date.plusDays(1);
                break;
            case WEEKLY:
                date = date.plusWeeks(1);
                break;
            case MONTHLY:
                date = date.plusMonths(1);
                break;
            case QUARTERLY:
                date = date.plusMonths(4);
                break;
            case YEARLY:
                date = date.plusYears(1);
                break;
            default:
                break;
        }

        return toTimestamp(date);
    }
}
