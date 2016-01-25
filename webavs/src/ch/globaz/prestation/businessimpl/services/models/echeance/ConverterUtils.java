package ch.globaz.prestation.businessimpl.services.models.echeance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class ConverterUtils {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Permet de creer une date depuis un string. La passé en paramétre doit être formaté de la manière suivante:
     * dd.MM.yyyy,
     * 
     * @param dateToParse
     * @param entity
     * @throws IllegalArgumentException
     *             si la date à parser n'est pas correct.
     * @return La date
     */
    public static Date parseDate(String dateToParse, Object entity) {

        if (dateToParse == null) {
            throw new IllegalArgumentException("the date is null for this:" + dateToParse + " [Object->" + entity + "]");
        }
        return ConverterUtils.parseDateWithNull(dateToParse, entity);
    }

    /**
     * Ideme que la fonctioN parse mais ne check pas si la valeur est null; Si la valeur est null la fonction renvera
     * null;
     * 
     * @param dateToParse
     * @param entity
     * @return
     */
    public static Date parseDateWithNull(String dateToParse, Object entity) {
        if ((dateToParse == null) || (dateToParse.trim().length() == 0)) {
            return null;
        }
        Date date = null;
        try {
            date = ConverterUtils.dateFormat.parse(dateToParse);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable to parse the date with this value:" + dateToParse + " [Object->"
                    + entity + "]", e);
        }
        return date;
    }

    /**
     * Formate une date au format suivant dd.MM.yyyy
     * 
     * @param date
     * @return la date formaté
     */
    public static String format(Date date, Object entity) {
        if (date == null) {
            throw new IllegalArgumentException("the date is null  [Object->" + entity + "]");
        }
        return ConverterUtils.formatWithNull(date);
    }

    /**
     * Formate une date au format suivant dd.MM.yyyy
     * 
     * @param date
     * @return la date formaté
     */
    public static String formatWithNull(Date date) {
        if (date == null) {
            return null;
        }
        return ConverterUtils.dateFormat.format(date);
    }
}
