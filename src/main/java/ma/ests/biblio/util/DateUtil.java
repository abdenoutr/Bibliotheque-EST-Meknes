package ma.ests.biblio.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String toString(LocalDate date) {
        if (date == null) return "";
        return date.format(FORMATTER);
    }

    public static LocalDate fromString(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isPast(LocalDate date) {
        if (date == null) return false;
        return date.isBefore(LocalDate.now());
    }

    public static LocalDate addDays(LocalDate date, int days) {
        if (date == null) return null;
        return date.plusDays(days);
    }

    public static LocalDate calculateReturnDate(LocalDate empruntDate, int dureeEnJours) {
        return addDays(empruntDate, dureeEnJours);
    }
}
