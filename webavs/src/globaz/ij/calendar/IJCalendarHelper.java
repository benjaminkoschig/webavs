package globaz.ij.calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Descpription
 * 
 * @author scr Date de création 12 sept. 05
 */
public class IJCalendarHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    private static final String[] shortDayNamesDE = new String[] { "MO", "DI", "MI", "DO", "FR", "SA", "SO" };

    /** DOCUMENT ME! */
    private static final String[] shortDayNamesEN = new String[] { "MO", "TU", "WE", "TH", "FR", "SA", "SU" };

    // table des noms de jours
    /** DOCUMENT ME! */
    private static final String[] shortDayNamesFR = new String[] { "LU", "MA", "ME", "JE", "VE", "SA", "DI" };

    /** DOCUMENT ME! */
    private static final String[] shortDayNamesIT = new String[] { "LU", "MA", "ME", "GI", "VE", "SA", "DO" };

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Le calendrier IJ commence le lundi, alors que le calendrier gregorian commence le dimanche !!!
     * 
     * <p>
     * LUN MAR MER JEU VEN SAM DIM Gregorian calendar : 2 3 4 5 6 7 1 IJ Calendar : 1 2 3 4 5 6 7
     * </p>
     * 
     * @param posJoursSemaine
     *            Position du jours de la semaine du calendrier Gregorian
     * 
     * @return Position du jours de la semaine du calendrier IJ
     */
    public static int convertPJSGregorianToIJCalendar(int posJoursSemaine) {
        if (posJoursSemaine == 1) {
            return 7;
        } else {
            return posJoursSemaine - 1;
        }
    }

    // crée une instance de Calendar en effacant les champs inutiles
    /**
     * getter pour l'attribut calendar instance
     * 
     * @return la valeur courante de l'attribut calendar instance
     */
    public static Calendar getCalendarInstance() {
        Calendar retValue = Calendar.getInstance();

        retValue.set(Calendar.HOUR_OF_DAY, 12);
        retValue.set(Calendar.MINUTE, 0);
        retValue.set(Calendar.SECOND, 0);
        retValue.set(Calendar.MILLISECOND, 0);

        return retValue;
    }

    /**
     * getter pour l'attribut short day name
     * 
     * Retourne le nom court du jours de la semaine, en fonction de la langue.
     * 
     * @param langue
     *            FR, DE, IT, EN
     * @param dayPos
     *            1==Lundi, 7==Dimanche
     * 
     * @return la valeur courante de l'attribut short day name
     */
    public static String getShortDayName(String langue, int dayPos) {

        if ("FR".equals(langue)) {
            return IJCalendarHelper.shortDayNamesFR[dayPos];
        } else if ("DE".equals(langue)) {
            return IJCalendarHelper.shortDayNamesDE[dayPos];
        } else if ("IT".equals(langue)) {
            return IJCalendarHelper.shortDayNamesIT[dayPos];
        } else if ("EN".equals(langue)) {
            return IJCalendarHelper.shortDayNamesEN[dayPos];
        } else {
            return IJCalendarHelper.shortDayNamesFR[dayPos];
        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @param df
     *            DOCUMENT ME!
     * @param date
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static Date parseDate(DateFormat df, String date) throws Exception {
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new Exception("Date invalide: " + date);
        }
    }
}
