package ch.globaz.common.util;

import ch.globaz.common.exceptions.Exceptions;
import globaz.globall.util.JADate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class Dates {
    private static final DateTimeFormatter DATE_TIME_SWISS_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME_DB_FORMATTER = DateTimeFormatter.ofPattern("yyyyddMM");

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

    public static String displayMonthFullnameYear(String swissFormatDate, String codeIso) {
        if (swissFormatDate == null || swissFormatDate.trim().isEmpty()) {
            return null;
        }
        LocalDate date = LocalDate.parse(swissFormatDate, DATE_TIME_SWISS_FORMATTER);
        Locale locale = new Locale(codeIso);
        return date.getMonth().getDisplayName(TextStyle.FULL, locale) + " " + date.getYear();
    }

    public static LocalDate toDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(date, DATE_TIME_SWISS_FORMATTER);
    }

    public static LocalDate toDateFromDb(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(date, DATE_TIME_DB_FORMATTER);
    }

    public static String toDbDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "0";
        }
        return Exceptions.checkedToUnChecked(() -> new JADate(date).toAMJ().toString(), "Error with this date:" + date);
    }

    public static LocalDate toDate(final JADate dateDebut) {
        return LocalDate.of(dateDebut.getYear(),dateDebut.getMonth(),dateDebut.getDay());
    }

    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null ? date1.isEqual(date2) : false;
    }

    public static long daysBetween(LocalDate fromDate, LocalDate toDate){
        long between = ChronoUnit.DAYS.between(fromDate, toDate);
        return between >= 0 ? between + 1 : between  - 1;
    }
}
