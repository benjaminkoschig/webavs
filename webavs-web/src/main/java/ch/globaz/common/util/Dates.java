package ch.globaz.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class Dates {
    private static final DateTimeFormatter DATE_TIME_SWISS_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Permet de formater une date au format Suisse.
     * Si la date est null une chaine vide est renvoyé.
     *
     * @param localDate La date.
     *
     * @return La date formaté.
     */
    public static String formatSwiss(TemporalAccessor localDate) {
        if (localDate == null) {
            return "";
        }
        return DATE_TIME_SWISS_FORMATTER.format(localDate);
    }

    public static String nowFormatSwiss() {
        return formatSwiss(LocalDate.now());
    }

    public static String displayMonthFullname(String swissFormatDate, String codeIso) {
        Locale locale = new Locale(codeIso);
        return LocalDate.parse(swissFormatDate, DATE_TIME_SWISS_FORMATTER).getMonth().getDisplayName(TextStyle.FULL, locale);
    }

    public static LocalDate toDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(date, DATE_TIME_SWISS_FORMATTER);
    }
}
