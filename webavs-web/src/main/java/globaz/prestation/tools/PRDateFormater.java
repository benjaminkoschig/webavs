/*
 * Créé le 2 juin 05
 */
package globaz.prestation.tools;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * <H1>Une classe pour formater des dates</H1>
 * 
 * @author scr
 */
public class PRDateFormater {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static final int YY_1947 = 47;

    /**
     * Date format converter
     * 
     * @param date
     * @return MMAA
     */
    public static String convertDate_AAAAMM_to_MMAA(String date) {

        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 6)) {
            String AA = date.substring(2, 4);
            String MM = date.substring(4, date.length());
            return MM + AA;
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return MMAAAA
     */
    public static String convertDate_AAAAMM_to_MMAAAA(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 6)) {
            String AAAA = date.substring(0, 4);
            String MM = date.substring(4, date.length());
            return MM + AAAA;
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return MM.AAAA
     */
    public static String convertDate_AAAAMM_to_MMxAAAA(String date) {

        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 6)) {
            String AAAA = date.substring(0, 4);
            String MM = date.substring(4, date.length());
            return MM + "." + AAAA;
        } else {
            return "";
        }
    }

    public static String convertDate_AAAAMMJJ_to_AAAAMM(String AAAAMMJJ) {
        if (JadeStringUtil.isBlankOrZero(AAAAMMJJ)) {
            return "";
        }

        try {
            JADate j = new JADate(new BigDecimal(AAAAMMJJ));

            return JACalendar.format(j, JACalendar.FORMAT_YYYYMM);
        } catch (JAException e) {
            return "";
        }
    }

    /**
     * Converti une date au formant AAAAMMJJ en JJMMAAAA
     * 
     * @param AAAAMMJJ
     *            La date a formater
     * 
     * @return la date formatée au format JJMMAAAA Aucun changement si Exception
     */
    public static String convertDate_AAAAMMJJ_to_JJMMAAAA(String AAAAMMJJ) {
        if (JadeStringUtil.isBlankOrZero(AAAAMMJJ)) {
            return "";
        }

        try {
            JADate j = new JADate(new BigDecimal(AAAAMMJJ));

            return JACalendar.format(j, JACalendar.FORMAT_DDMMYYYY);
        } catch (JAException e) {
            return "";
        }
    }

    /**
     * Converti une date au formant jj.mm.aaaa en AAAAMM
     * 
     * @param AAAAMMJJ la date à formater
     * @return la date formatée au format AAAAMM Aucun changement si Exception
     */
    public static String convertDate_AAAAMMJJ_to_JJxMMxAAAA(String AAAAMMJJ) {
        if (JadeStringUtil.isBlankOrZero(AAAAMMJJ)) {
            return "";
        }

        try {
            JADate j = new JADate(new BigDecimal(AAAAMMJJ));

            return JACalendar.format(j, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (JAException e) {
            return "";
        }
    }

    /**
     * Converti une date au formant jj.mm.aaaa en AAAAMM
     * 
     * @param AAAAMMJJ la date à formater
     * @return la date formatée au format AAAAMM Aucun changement si Exception
     */
    public static String convertDate_AAAAMMJJ_to_MMxAAAA(String AAAAMMJJ) {
        if (JadeStringUtil.isBlankOrZero(AAAAMMJJ)) {
            return "";
        }
        if (AAAAMMJJ.length() != 8) {
            return "";
        }

        try {
            JADate j = new JADate(new BigDecimal(AAAAMMJJ));

            return JACalendar.format(j, JACalendar.FORMAT_MMsYYYY);
        } catch (JAException e) {
            return "";
        }
    }

    /**
     * Formate une date à partir d'une String au format AAAA.MM.
     * 
     * @param AAAAxMM
     *            La date a formater
     * 
     * @return la date formatée au format MMAA
     */
    public static String convertDate_AAAAxMM_to_MMAA(String AAAAxMM) {
        if (JadeStringUtil.isBlankOrZero(AAAAxMM)) {
            return "";
        }

        if ((AAAAxMM != null) && (AAAAxMM.length() == 7)) {
            return AAAAxMM.substring(5, 7) + AAAAxMM.substring(2, 4);
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AAAAMM
     */
    public static String convertDate_AAMM_to_AAAAMM(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 4)) {

            String AA = date.substring(0, 2);
            String MM = date.substring(2, 4);

            int aa = Integer.parseInt(AA);
            if (aa >= PRDateFormater.YY_1947) {
                result = "19" + AA + MM;
            } else {
                result = "20" + AA + MM;
            }
            return result;
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AAxMM
     */
    public static String convertDate_AAMM_to_AAxMM(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 4)) {

            String AA = date.substring(0, 2);
            String MM = date.substring(2, 4);

            result = AA + "." + MM;

            return result;
        } else {
            return "";
        }
    }

    /**
     * Converti une date au format aammjj en jj.mm.aaaa, concerne uniquement les années >= 2000
     * 
     * @param AAMMJJ
     *            AAMMJJ La date à formater
     * 
     * @return la date formatée (jj.mm.aaaa)
     */
    public static String convertDate_AAMMJJ_to_JJxMMxAAAA(final String AAMMJJ) throws JAException {

        final String ANNEE_DEUXMILLES_DEUX_POSITIONS = "20";

        if (JadeStringUtil.isBlankOrZero(AAMMJJ)) {
            throw new JAException("PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA()", "Date vide");
        }

        if (AAMMJJ.length() != 6) {
            throw new JAException("PRDateFormater.convertDate_AAMMJJ_to_JJxMMxAAAA()", "Taille différente de 6");
        }

        JADate j = new JADate(new BigDecimal(ANNEE_DEUXMILLES_DEUX_POSITIONS + AAMMJJ));

        return JACalendar.format(j, JACalendar.FORMAT_DDsMMsYYYY);
    }

    /**
     * Formate une date à partir d'une String au format AA.MM.
     * 
     * @param AAxMM
     *            La date a formater
     * 
     * @return la date formatée au format AAMM
     */
    public static String convertDate_AAxMM_to_AAMM(String AAxMM) {
        if (JadeStringUtil.isBlankOrZero(AAxMM)) {
            return "";
        }

        if ((AAxMM != null) && (AAxMM.length() == 5)) {
            return AAxMM.substring(0, 2) + AAxMM.substring(3, 5);
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AxM
     */
    public static String convertDate_AM_to_AxM(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 2)) {

            String A = date.substring(0, 1);
            String M = date.substring(1, 2);

            result = A + "." + M;

            return result;
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AxMM
     */
    public static String convertDate_AMM_to_AxMM(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 3)) {

            String A = date.substring(0, 1);
            String MM = date.substring(1, 3);

            result = A + "." + MM;

            return result;
        } else {
            return "";
        }
    }

    /**
     * Formate une date à partir d'une String au format JJ.MM.AAAA.
     * 
     * @param JJMMAAAA
     * @return la date formatée au format MMMM AAAA
     * @throws JAException
     */

    public static String convertDate_JJMMAAAA_to_MMMMAAAA(String JJMMAAAA) throws JAException {
        if (JadeStringUtil.isBlankOrZero(JJMMAAAA)) {
            return "";
        }

        if ((JJMMAAAA != null) && (JJMMAAAA.length() == 8)) {
            JADate j = new JADate(JJMMAAAA);
            int i = j.getMonth();
            String MMMM = JACalendar.getMonthName(i);
            String AAAA = JJMMAAAA.substring(4, JJMMAAAA.length());

            return MMMM + " " + AAAA;
        } else {
            return "";
        }

    }

    /**
     * Formate une date à partir d'une String au format JJ.MM.AAAA.
     * 
     * @param JJxMMxAAAA
     * @return la date formatée au format JJ MMMM AAAA
     */

    public static String convertDate_JJxMMxAAAA_to_AAAA(String JJxMMxAAAA) {
        if (JadeStringUtil.isBlankOrZero(JJxMMxAAAA)) {
            return "";
        }

        if ((JJxMMxAAAA != null) && (JJxMMxAAAA.length() == 10)) {
            String AAAA = JJxMMxAAAA.substring(6, JJxMMxAAAA.length());

            return AAAA;
        } else {
            return "";
        }

    }

    /**
     * Formate une date à partir d'une String au format AAAAMM.
     * 
     * @param JJxMMxAAAA
     * @return la date formatée au format AAAAMM Aucun changement si Exception
     * @throws JAException
     */

    public static String convertDate_JJxMMxAAAA_to_AAAAMM(String JJxMMxAAAA) throws JAException {
        if (JadeStringUtil.isBlankOrZero(JJxMMxAAAA)) {
            return "";
        }

        JADate j = new JADate(JJxMMxAAAA);
        return JACalendar.format(j, JACalendar.FORMAT_YYYYMM);
    }

    /**
     * Formate une date à partir d'une String au format AAAAMMJJ.
     * 
     * @param JJxMMxAAAA
     * @return la date formatée au format AAAAMMJJ Aucun changement si Exception
     * @throws JAException
     */
    public static String convertDate_JJxMMxAAAA_to_AAAAMMJJ(String JJxMMxAAAA) throws JAException {
        if (JadeStringUtil.isBlankOrZero(JJxMMxAAAA)) {
            return "";
        }

        JADate j = new JADate(JJxMMxAAAA);
        return JACalendar.format(j, JACalendar.FORMAT_YYYYMMDD);
    }

    /**
     * Formate une date à partir d'une String au format JJ.MM.AAAA.
     * 
     * @param JJxMMxAAAA
     * @return la date formatée au format JJ MMMM AAAA
     * @throws JAException
     */

    public static String convertDate_JJxMMxAAAA_to_JJMMMMAAAA(String JJxMMxAAAA) throws JAException {
        if (JadeStringUtil.isBlankOrZero(JJxMMxAAAA)) {
            return "";
        }

        if ((JJxMMxAAAA != null) && (JJxMMxAAAA.length() == 10)) {
            JADate j = new JADate(JJxMMxAAAA);
            String JJ = JJxMMxAAAA.substring(0, 2);
            int i = j.getMonth();
            String MMMM = JACalendar.getMonthName(i);
            String AAAA = JJxMMxAAAA.substring(6, JJxMMxAAAA.length());

            return JJ + " " + MMMM + " " + AAAA;
        } else {
            return "";
        }

    }

    public static String convertDate_JJxMMxAAAA_to_JJxMMxAA(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 10)) {
            String s = date.substring(0, 5);
            s += "." + date.substring(8, 10);

            return s;
        } else {
            return "";
        }
    }

    /**
     * Formate une date à partir d'une String au format JJ.MM.AAAA.
     * 
     * @param JJxMMxAAAA
     * @return la date formatée au format AAAAMM Aucun changement si Exception
     * @throws JAException
     */

    public static String convertDate_JJxMMxAAAA_to_MMxAAAA(String JJxMMxAAAA) throws JAException {
        if (JadeStringUtil.isBlankOrZero(JJxMMxAAAA)) {
            return "";
        }

        JADate j = new JADate(JJxMMxAAAA);
        return JACalendar.format(j, JACalendar.FORMAT_MMsYYYY);
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AAAA.MM
     */
    public static String convertDate_MMAA_to_AAAAMM(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 4)) {
            String AA = date.substring(2, 4);
            String MM = date.substring(0, 2);

            int aa = Integer.parseInt(AA);
            if (aa >= PRDateFormater.YY_1947) {
                result = "19" + AA + MM;
            } else {
                result = "20" + AA + MM;
            }
            return result;
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AAAA.MM
     */
    public static String convertDate_MMAA_to_AAAAxMM(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 4)) {
            String AA = date.substring(2, 4);
            String MM = date.substring(0, 2);

            int aa = Integer.parseInt(AA);
            if (aa >= PRDateFormater.YY_1947) {
                result = "19" + AA + "." + MM;
            } else {
                result = "20" + AA + "." + MM;
            }
            return result;
        } else {
            return "";
        }
    }

    /**
     * Date format converter
     * 
     * @param date
     * @return AAAA.MM
     */
    public static String convertDate_MMAA_to_MMxAAAA(String date) {

        String result = "";
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        } else if ((date != null) && (date.length() == 4)) {
            String AA = date.substring(2, 4);
            String MM = date.substring(0, 2);

            int aa = Integer.parseInt(AA);
            if (aa >= PRDateFormater.YY_1947) {
                result = MM + "." + "19" + AA;
            } else {
                result = MM + "." + "20" + AA;
            }
            return result;
        } else {
            return "";
        }
    }

    public static String convertDate_MMAAAA_to_AAAAMM(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 6)) {
            String MM = date.substring(0, 2);
            String AAAA = date.substring(2, date.length());
            return AAAA + MM;
        } else {
            return "";
        }
    }

    public static String convertDate_MMAAAA_to_MMxAAAA(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 6)) {
            String MM = date.substring(0, 2);
            String AAAA = date.substring(2, date.length());
            return MM + "." + AAAA;
        } else {
            return "";
        }
    }

    /**
     * Formate une date à partir d'une String au format MM.AAAA.
     * 
     * @param MMxAAAA
     * @return la date formatée au format AAAA
     */

    public static String convertDate_MMxAAAA_to_AAAA(String MMxAAAA) {
        if (JadeStringUtil.isBlankOrZero(MMxAAAA)) {
            return "";
        }

        if ((MMxAAAA != null) && (MMxAAAA.length() == 7)) {
            String AAAA = MMxAAAA.substring(3, MMxAAAA.length());

            return AAAA;
        } else {
            return "";
        }

    }

    /**
     * Date Format converter
     * 
     * @param date
     * @return AAAAMM
     */
    public static String convertDate_MMxAAAA_to_AAAAMM(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 7)) {
            String MM = date.substring(0, date.indexOf("."));
            String AAAA = date.substring(date.indexOf(".") + 1, date.length());

            return AAAA + MM;
        } else {
            return "";
        }

    }

    /**
     * Date Format converter
     * 
     * @param date
     * @return MMAA
     */
    public static String convertDate_MMxAAAA_to_MMAA(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 7)) {
            String MM = date.substring(0, date.indexOf("."));
            String AA = date.substring(date.indexOf(".") + 3, date.length());
            return MM + AA;
        } else {
            return "";
        }
    }

    /**
     * Date Format converter
     * 
     * @param date
     * @return MMAAA
     */
    public static String convertDate_MMxAAAA_to_MMAAAA(String date) {
        if (JadeStringUtil.isBlankOrZero(date)) {
            return "";
        }

        if ((date != null) && (date.length() == 7)) {
            String MM = date.substring(0, date.indexOf("."));
            String AAAA = date.substring(date.indexOf(".") + 1, date.length());

            return MM + AAAA;
        } else {
            return "";
        }
    }

    /**
     * Converti une date au formant jj.mm.aaaa en AAAAMM
     * 
     * @param AAAAMMJJ
     * @return la date formatée au format AAAAMM Aucun changement si Exception
     */
    public static String convertDateToAAAAMM(String AAAAMMJJ) {
        if (JadeStringUtil.isBlankOrZero(AAAAMMJJ)) {
            return "";
        }

        try {
            JADate j = new JADate(new BigDecimal(AAAAMMJJ));

            return JACalendar.format(j, JACalendar.FORMAT_DDsMMsYYYY);
        } catch (JAException e) {
            return AAAAMMJJ;
        }
    }

    /**
     * Renvoie une chaine formatée contenant une date sous la forme "mmm yyyy". (octobre 2007)
     * 
     * @return la chaine formatée
     * @param date
     *            la date
     * @param language
     *            la langue (au format ISO)
     */
    public static String format_MMMYYYY(JADate date, String language) {

        String STRING_0 = "0";

        if (date == null) {
            return "";
        }
        if ((date.getDay() == 0) && (date.getMonth() == 0) && (date.getYear() == 0)) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        // buffer.append(date.getDay());
        // if (date.getDay() == 1) {
        // if (LANGUAGE_FR.equalsIgnoreCase(language)) {
        // buffer.append("er");
        // }
        // }
        // if (LANGUAGE_DE.equalsIgnoreCase(language)) {
        // buffer.append(".");
        // }
        // buffer.append(" ");
        try {
            buffer.append(JACalendar.getMonthName(date.getMonth(), language));
        } catch (Exception e) {
            JadeCodingUtil.catchException(JACalendar.class, "format_DMMMYYY", e);
        }
        buffer.append(" ");
        if (date.getYear() < 1000) {
            buffer.append(STRING_0);
        }
        if (date.getYear() < 100) {
            buffer.append(STRING_0);
        }
        if (date.getYear() < 10) {
            buffer.append(STRING_0);
        }
        buffer.append(date.getYear());

        return buffer.toString();
    }

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
        return PRDateFormater.getDateFormatInstance(session.getIdLangueISO(), pattern);
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

    /**
     * Calcule le nombre de mois entre 2 dates.
     * 
     * @param date1
     *            une date au format MM.AAAA
     * @param date2
     *            une autre date au format MM.AAAA
     * 
     * @return un entier valant le nombre de mois entre les dates Si mois début == janvier et mois fin == janvier,
     *         retourne 1. Si mois début == janvier et mois fin == février, retourne 2.
     * @throws JAException
     *             Si les dates ne sont pas au bon format
     * 
     * 
     */
    public static int nbrMoisEntreDates(JADate date1, JADate date2) throws JAException {

        int m1 = date1.getMonth();
        int m2 = date2.getMonth();

        int y1 = date1.getYear();
        int y2 = date2.getYear();

        if (y2 == y1) {
            return (m2 - m1) + 1;
        } else {
            int diffYear = y2 - y1;
            if (diffYear == 1) {
                return ((12 - m1) + 1) + m2;
            } else {
                int tmp = ((12 - m1) + 1) + m2;
                tmp = tmp + (12 * (diffYear - 1));
                return tmp;
            }
        }

    }

}
