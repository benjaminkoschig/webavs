package globaz.aquila.util;

import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.config.COConfigurationService;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Calendar;

/**
 * Utilitaires pour les dates
 * 
 * @author Arnaud Dostes, 27-oct-2004
 */
public final class CODateUtils {

    /** Le calendrier utilisé pour les opérations */
    private static JACalendar calendar = new JACalendarGregorian();
    /** utilisé pour charger la date de délai **/
    private static final COConfigurationService CONFIG_SERVICE = COServiceLocator.getConfigService();
    /**
     * date de délai par défaut dans le cas ou le config_service ne retourne rien
     **/
    private static final double DATEDELAIDEFAULT = 30.00;

    /**
     * @see JACalendar#compare(String, String)
     */
    public static int compare(String date1, String date2) throws JAException {
        return CODateUtils.calendar.compare(date1, date2);
    }

    /**
     * Renvoie le calendrier
     * 
     * @return Le calendrier
     */
    public static JACalendar getCalendar() {
        return CODateUtils.calendar;
    }

    /**
     * Renvoie la date au format DD
     * 
     * @param date
     *            La date
     * @return La date au format DD
     */
    public static String getDateDD(JADate date) {
        return JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY).substring(0, 2);
    }

    /**
     * Renvoie la date au format DD
     * 
     * @param date
     *            La date
     * @return La date au format DD
     */
    public static String getDateDD(String date) {
        return JACalendar.format(date, JACalendar.FORMAT_DDsMMsYYYY).substring(0, 2);
    }

    /**
     * @param session
     * @return la date prenant en compte le délai de paiements configuré dans FWPARP sinon 30
     */
    public static String getDateDelaiPaiement(BSession session) {
        String dateDelaiPaiement;
        try {
            dateDelaiPaiement = CODateUtils.getTodayPlusDaysDDsMMsYYYY((int) CODateUtils.CONFIG_SERVICE.getOption(
                    session, COConfigurationService.DELAI_PAIEMENT).doubleValue());
        } catch (Exception e) {
            dateDelaiPaiement = CODateUtils.getTodayPlusDaysDDsMMsYYYY((int) CODateUtils.DATEDELAIDEFAULT);
        }
        return dateDelaiPaiement;
    }

    /**
     * Renvoie la date au format JJMMAAAA
     * 
     * @param c
     *            date La date
     * @return La date au format DD
     */
    public static String getDateJJMMAAAA(Calendar c, char separator) {
        String jj = CODateUtils.padStringBefore("" + c.get(Calendar.DAY_OF_MONTH), 2, '0');
        String mm = CODateUtils.padStringBefore("" + (c.get(Calendar.MONTH) + 1), 2, '0');
        String aaaa = "" + c.get(Calendar.YEAR);
        return jj + separator + mm + separator + aaaa;
    }

    /**
     * Renvoie la date au format MM
     * 
     * @param date
     *            La date
     * @return La date au format MM
     */
    public static String getDateMM(JADate date) {
        return JACalendar.format(date, JACalendar.FORMAT_MMsYYYY).substring(0, 2);
    }

    /**
     * Renvoie la date au format MM
     * 
     * @param date
     *            La date
     * @return La date au format MM
     */
    public static String getDateMM(String date) {
        if (!JadeStringUtil.isBlankOrZero(date) && (date.length() > 2)) {
            return JACalendar.format(date, JACalendar.FORMAT_MMsYYYY).substring(0, 2);
        } else {
            return "";
        }
    }

    /**
     * Renvoie la date + le nombre de jours spécifiés
     * 
     * @param date
     *            La date
     * @param days
     *            Le nombre de jours
     * @return La date
     */
    public static JADate getDatePlusDays(JADate date, int days) {
        return CODateUtils.calendar.addDays(date, days);
    }

    /**
     * Renvoie la date + le nombre de jours spécifiés
     * 
     * @param date
     *            La date
     * @param days
     *            Le nombre de jours
     * @return La date au format DDsMMsYYYY
     */
    public static String getDatePlusDaysDDsMMsYYYY(JADate date, int days) {
        return JACalendar.format(CODateUtils.getDatePlusDays(date, days), JACalendar.FORMAT_DDsMMsYYYY);
    }

    /**
     * Renvoie la date + le nombre de jours spécifiés
     * 
     * @param date
     *            La date
     * @param days
     *            Le nombre de jours
     * @return La date au format YYYYMMDD
     */
    public static String getDatePlusDaysYYYYMMDD(JADate date, int days) {
        return JACalendar.format(CODateUtils.getDatePlusDays(date, days), JACalendar.FORMAT_YYYYMMDD);
    }

    /**
     * Renvoie la date au format YYYY
     * 
     * @param date
     *            La date
     * @return La date au format YYYY
     */
    public static String getDateYYYY(JADate date) {
        return JACalendar.format(date, JACalendar.FORMAT_YYYY);
    }

    /**
     * Renvoie la date au format YYYY
     * 
     * @param date
     *            La date
     * @return La date au format YYYY
     */
    public static String getDateYYYY(String date) {
        return JACalendar.format(date, JACalendar.FORMAT_YYYY);
    }

    /**
     * Renvoie la date d'aujourd'hui + le nombre de jours spécifiés
     * 
     * @param days
     *            Le nombre de jours
     * @return La date
     */
    public static JADate getTodayPlusDays(int days) {
        return CODateUtils.calendar.addDays(JACalendar.today(), days);
    }

    /**
     * Renvoie la date d'aujourd'hui + le nombre de jours spécifiés
     * 
     * @param days
     *            Le nombre de jours
     * @return La date au format DDsMMsYYYY
     */
    public static String getTodayPlusDaysDDsMMsYYYY(int days) {
        return JACalendar.format(CODateUtils.getTodayPlusDays(days), JACalendar.FORMAT_DDsMMsYYYY);
    }

    /**
     * Renvoie la date d'aujourd'hui + le nombre de jours spécifiés
     * 
     * @param days
     *            Le nombre de jours
     * @return La date au format YYYYMMDD
     */
    public static String getTodayPlusDaysYYYYMMDD(int days) {
        return JACalendar.format(CODateUtils.getTodayPlusDays(days), JACalendar.FORMAT_YYYYMMDD);
    }

    /**
     * retourne vrai si la date est une date valide dans le calendrier grégorien.
     */
    public static boolean isValid(String date) {
        return CODateUtils.getCalendar().isValid(date);
    }

    public static String padStringBefore(String stringToPad, int length, char charToPad) {
        while (stringToPad.length() < length) {
            stringToPad = charToPad + stringToPad;
        }
        return stringToPad;
    }

    /**
     * Constructeur privé
     */
    private CODateUtils() {
    }
}
