/*
 * Créé le 2 juin 05
 */
package globaz.hera.tools;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * <H1>Une classe pour formater des dates</H1>
 * 
 * @author dvh
 */
public class SFDateFormater {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Formate une date à partir d'une String au format AAAAMMJJ.
     * 
     * @param AAAAMMJJ
     *            La date a formater
     * 
     * @return la date formatée au format JJ.MM.AAAA Aucun changement si Exception
     */
    public static String formatDateFrom(String AAAAMMJJ) {
        try {
            JADate j = new JADate(new BigDecimal(AAAAMMJJ));

            return JACalendar.format(j, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (JAException e) {
            return AAAAMMJJ;
        }
    }

    /**
     * Formate une date à partir d'une String au format AAAAMMJJ.
     * 
     * @param AAAAMMJJ
     *            La date a formater
     * @param separator
     *            Le séparateur à utiliser
     * 
     * @return la date formatée au format JJ(sep)MM(sep)AAAA. Aucun changement si Exception
     * 
     * @deprecated utiliser formatDateFrom(String)
     */
    @Deprecated
    public static String formatDateFrom(String AAAAMMJJ, String separator) {
        try {
            return AAAAMMJJ.substring(6, 8) + separator + AAAAMMJJ.substring(4, 6) + separator
                    + AAAAMMJJ.substring(0, 4);
        } catch (Exception e) {
            return AAAAMMJJ;
        }
    }

    /**
     * Cree une instance de date format pour la langue de la session
     * 
     * @param session
     *            la session avec la langue
     * @param pattern
     *            le pattern de mis en forme des dates
     * 
     * @return la valeur courante de l'attribut date format instance
     * 
     * @see
     */
    public static DateFormat getDateFormatInstance(BSession session, String pattern) {
        return getDateFormatInstance(session.getIdLangueISO(), pattern);
    }

    /**
     * Cree une instance de date format pour la langue specifiee
     * 
     * @param codeIsoLangue
     *            la session avec la langue
     * @param pattern
     *            le pattern de mis en forme des dates
     * 
     * @return la valeur courante de l'attribut date format instance
     * 
     * @see
     */
    public static DateFormat getDateFormatInstance(String codeIsoLangue, String pattern) {
        return new SimpleDateFormat(pattern, new Locale(codeIsoLangue, "CH"));
    }

    /**
     * Calcule le nombre de jours entre 2 dates, quelque soit l'ordre. Les bornes sont incluses, autrement dit, si les 2
     * dates sont les mêmes, la différence sera de 1.
     * 
     * @param date1Formatee
     *            une date au format JJ.MM.AAAA
     * @param date2Formatee
     *            une autre date au format JJ.MM.AAAA
     * 
     * @return un entier valant le nombre de jours entre les dates
     * 
     * @throws JAException
     *             Si les dates ne sont pas au bon format
     * 
     * @deprecated utiliser une instance de JACalendar et la méthode daysBetween
     */
    @Deprecated
    public static int nbrJoursEntreDates(String date1Formatee, String date2Formatee) throws JAException {
        JADate date1 = new JADate(date1Formatee);
        JADate date2 = new JADate(date2Formatee);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(date1.getYear(), date1.getMonth() - 1, date1.getDay());

        long msDate1 = calendar.getTime().getTime();

        calendar.clear();
        calendar.set(date2.getYear(), date2.getMonth() - 1, date2.getDay());

        long msDate2 = calendar.getTime().getTime();

        long diff = Math.abs(msDate1 - msDate2);

        int nbJoursDiff = Integer.parseInt(Long.toString((diff / (1000 * 60 * 60 * 24)) + 1));

        return nbJoursDiff;
    }
}
