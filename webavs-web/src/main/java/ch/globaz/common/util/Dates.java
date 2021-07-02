package ch.globaz.common.util;

import ch.globaz.common.exceptions.Exceptions;
import globaz.globall.util.JADate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

/**
 * Class utilitaire pour travailler avec les LocalDate.
 */
public class Dates {
    private static final DateTimeFormatter DATE_TIME_SWISS_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME_DB_FORMATTER = DateTimeFormatter.ofPattern("yyyyddMM");

    /**
     * Permet de formater une date au format Suisse.
     * Si la date est null une chaine vide est renvoy�.
     *
     * @param localDate La date.
     *
     * @return La date format�.
     */
    public static String formatSwiss(TemporalAccessor localDate) {
        if (localDate == null) {
            return "";
        }
        return DATE_TIME_SWISS_FORMATTER.format(localDate);
    }

    /**
     * @return Renvoi la date de maitenant au format suisse.
     */
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

    /**
     * Permet de convertir une date en string avec la format suivant: dd.MM.yyyy
     *
     * @param date La date � convertir(01.02.2021).
     *
     * @return Le nombre de jours.
     */
    public static LocalDate toDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(date, DATE_TIME_SWISS_FORMATTER);
    }

    /**
     * Permet de convertir une date en string avec la format suivant: yyyyMMdd.
     * Ce format est surtout utilis� par la db.
     *
     * @param date La date � convertir(20210102).
     *
     * @return Le nombre de jours.
     */
    public static LocalDate toDateFromDb(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(date, DATE_TIME_DB_FORMATTER);
    }

    /**
     * Permet de convertir une date en string avec la format suivant: dd.MM.yyyy
     *
     * @param date La date � convertir(01.02.2021).
     *
     * @return Le nombre de jours.
     */
    public static String toDbDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "0";
        }
        return Exceptions.checkedToUnChecked(() -> new JADate(date).toAMJ().toString(), "Error with this date:" + date);
    }

    /**
     * Permet de convertir une JADate en LocalDate.
     *
     * @param date JADate.
     *
     * @return La date en LocalDate.
     */
    public static LocalDate toDate(final JADate dateDebut) {
        return LocalDate.of(dateDebut.getYear(), dateDebut.getMonth(), dateDebut.getDay());
    }

    /**
     * Convertie une LocalDate en JaDate.
     *
     * @param date La date � convertir.
     *
     * @return La date converti.
     */
    public static JADate toJADate(final LocalDate date) {
        return new JADate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    /**
     * Convertie une LocalDate en Date.
     *
     * @param localDate La date � convertir.
     *
     * @return La date converti.
     */
    public static Date toJavaDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null ? date1.isEqual(date2) : false;
    }

    /**
     * Permet de calculer le nombre de jour entre deux dates.
     * 01.02.2021 et 01.02.2021 => 1 jours.
     *
     * @param fromDate La date de d�but au format suisse (01.02.2021)
     * @param toDate   La date de fin au format suisse (01.02.2021)
     *
     * @return Le nombre de jours.
     */
    public static long daysBetween(String fromDate, String toDate) {
        return daysBetween(Dates.toDate(fromDate), Dates.toDate(toDate));
    }

    /**
     * Permet de calculer le nombre de jour entre deux dates.
     * 01.02.2021 et 01.02.2021 => 1 jours.
     *
     * @param fromDate La date de d�but.
     * @param toDate   La date de fin.
     *
     * @return Le nombre de jours.
     */
    public static long daysBetween(LocalDate fromDate, LocalDate toDate) {
        long between = ChronoUnit.DAYS.between(fromDate, toDate);
        return between >= 0 ? between + 1 : between - 1;
    }

}
