package globaz.hermes.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Classe d'utilitaires de date utilisant le package java.text et<br>
 * la classe java.util.Calendar La date utilisée dans les commentaires est celle du 10.05.2005 Date de création :
 * (11.03.2003 14:12:50)
 *
 * @author: ado
 * @deprecated Devrait être dans le FW
 */
@Deprecated
public class DateUtils {
    /**
     * Format de date inversée à 8 positions, sans points : 20050510
     */
    public final static String AAAAMMJJ = "yyyyMMdd";
    /**
     * Format de date inversée à 6 positions, sans points : 050510
     */
    public final static String AAMMJJ = "yyMMdd";
    /**
     * Format de date avec le jour et le mois, sans point : 1005
     */
    public final static String JJMM = "ddMM";
    /**
     * Format de date avec le jour et le mois, avec point : 10.05
     */
    public final static String JJMM_DOTS = "dd.MM";
    /**
     * Format de date avec année sur deux positions : 100505
     */
    public final static String JJMMAA = "ddMMyy";
    /**
     * Format de date avec année sur deux positions avec points 10.05.05
     */
    public final static String JJMMAA_DOTS = "dd.MM.yy";
    /**
     * Format de date sans points : 10052005
     */
    public final static String JJMMAAAA = "ddMMyyyy";
    /**
     * Format de date avec points : 10.05.2005
     */
    public final static String JJMMAAAA_DOTS = "dd.MM.yyyy";
    /**
     * Langue DE
     */
    public final static String LANGUE_DE = "DE";
    /**
     * Langue FR
     */
    public final static String LANGUE_FR = "FR";

    /**
     * Langue IT
     */
    public final static String LANGUE_IT = "IT";
    /**
     * Format avec mois et année sur 2 : 0505
     */
    public final static String MMAA = "MMyy";

    /**
     * Format avec mois et année sur deux avec points : 05.05
     */
    public final static String MMAA_DOTS = "MM.yy";
    /**
     * Format de date avec mois et année sur 4 sans points : 052005
     */
    public final static String MMAAAA = "MMyyyy";
    /**
     * Format de date avec mois et année sur 4 avec points : 05.2005
     */
    public final static String MMAAAA_DOTS = "MM.yyyy";

    /**
     * Convertit une date d'une format vers un autre<br>
     * On peux utiliser les patterns de java.text.SimpleDateFormat
     *
     * @param value
     *            la date à convertir
     * @param fromFormat
     *            le format d'origine (cf constantes)
     * @param toFormat
     *            le format de destination
     * @return la date convertie
     */
    public static String convertDate(String value, String fromFormat, String toFormat) {
        value = StringUtils.removeDots(value);
        if (fromFormat.equals(DateUtils.JJMMAAAA_DOTS)) {
            fromFormat = DateUtils.JJMMAAAA;
        } else if (fromFormat.equals(DateUtils.JJMMAA_DOTS)) {
            fromFormat = DateUtils.JJMMAA;
        } else if (fromFormat.equals(DateUtils.MMAA_DOTS)) {
            fromFormat = DateUtils.MMAA;
        }
        try {
            SimpleDateFormat from = new SimpleDateFormat(fromFormat);
            from.setLenient(false);
            Date d = from.parse(value);
            SimpleDateFormat to = new SimpleDateFormat(toFormat);
            to.setLenient(false);
            return to.format(d);
        } catch (Exception e) {
            // e.printStackTrace();
            // globaz.hermes.application.HEApplication.getLogger().error(e.toString());
            return value;
        }
    }

    /**
     * Convertit une date avec une année sur 2 chiffres à une date avec<br>
     * l'année sur 4 chiffres.
     *
     * @param dateToFormat
     *            la date à convertir au format AAMMJJ
     * @return L'année sur 4 positions : AAAAMMJJ
     */
    public static String dateAAtoDateAAAA(String dateToFormat) {
        // String retour = "";
        String currentYear = "" + Calendar.getInstance().get(Calendar.YEAR);
        currentYear = currentYear.substring(2, 4);
        if ((Integer.parseInt(dateToFormat) >= 0)
                && (Integer.parseInt(dateToFormat) <= Integer.parseInt(currentYear))) {
            return "20" + dateToFormat;
        } else {
            return "19" + dateToFormat;
        }
    }

    /**
     * Convertit une date JJMMAA en JJMMAAAA
     *
     * @param dateToFormat
     *            la date à convertir
     * @return la date convertie
     */
    public static String dateJJMMAAtoDateJJMMAAAA(String dateToFormat) {
        // String retour = "";
        String currentYear = "" + Calendar.getInstance().get(Calendar.YEAR);
        currentYear = currentYear.substring(2, 4);
        String dateToFormatYear = dateToFormat.substring(4, 6);
        if ((0 <= Integer.parseInt(dateToFormatYear))
                && (Integer.parseInt(dateToFormatYear) <= Integer.parseInt(currentYear))) {
            // 19
            return dateToFormat.substring(0, 4) + "20" + dateToFormatYear;
        } else {
            // 20
            return dateToFormat.substring(0, 4) + "19" + dateToFormatYear;
        }
    }

    /**
     * Passe une année sur deux chiffres à quatre chiffres
     *
     * @param aa
     *            L'année
     * @return L'année sur 4 chiffres
     * @throws NumberFormatException
     *             si invalide
     * @throws ParseException
     *             si invalide
     */
    public static int enlargeYear(int aa) throws ParseException {
        String dateCourante = DateUtils.getCurrentDateAMJ();
        int dateCouranteAASecond = Integer
                .parseInt(DateUtils.getYear(dateCourante, DateUtils.AAAAMMJJ).substring(1, 4));
        int dateCouranteAAFirst = Integer.parseInt(DateUtils.getYear(dateCourante, DateUtils.AAAAMMJJ).substring(0, 2));
        if (aa > (dateCouranteAASecond + 1)) {
            // si c'est plus grand, c'est à dire que c'est le siècle passé
            return ((dateCouranteAAFirst - 1) * 100) + aa;
        } else {
            // si c'est plus petit ou égal, c'est a dire que c'est le même
            // siècle que l'année en cours
            return (dateCouranteAAFirst * 100) + aa;
        }
    }

    /**
     * Renvoi un Calendar en fonction d'une date
     *
     * @param date
     *            La date
     * @param fromFormat
     *            le format de la date
     * @return un objet Calendar reglé à la date passée en paramètre
     */
    public static Calendar getCalendarFromDate(String date, String fromFormat) {
        try {
            SimpleDateFormat from = new SimpleDateFormat(fromFormat);
            from.setLenient(false);
            Date d = from.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal;
            /*
             * SimpleDateFormat to = new SimpleDateFormat(toFormat); to.setLenient(false); return to.format(d);
             */
        } catch (Exception e) {
            // globaz.hermes.application.HEApplication.getLogger().error(e.toString());
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Renvoit la date du jour
     *
     * @return la date sous forme AAAAMMJJ : 20050510
     */
    public static String getCurrentDateAMJ() {
        String returnString = "";
        java.util.Calendar c = java.util.Calendar.getInstance();
        returnString += c.get(java.util.Calendar.YEAR);
        if ((c.get(java.util.Calendar.MONTH) + 1) < 10) {
            returnString += "0" + (c.get(java.util.Calendar.MONTH) + 1);
        } else {
            returnString += c.get(java.util.Calendar.MONTH) + 1;
        }
        if ((c.get(java.util.Calendar.DAY_OF_MONTH)) < 10) {
            returnString += "0" + (c.get(java.util.Calendar.DAY_OF_MONTH));
        } else {
            returnString += c.get(java.util.Calendar.DAY_OF_MONTH);
        }
        return returnString;
    }

    /**
     * Renvoit l'heure système courante
     *
     * @return l'heure (ex : 08:40)
     */
    public static String getCurrentTime() {
        return DateUtils.getCurrentTime(Calendar.getInstance());
    }

    /**
     * Renvoit l'heure courante en fonction de l'objet Calendar
     *
     * @param cal
     *            le calendrier
     * @return l'heure courant (ex : 08:40)
     */
    public static String getCurrentTime(Calendar cal) {
        DateFormat f = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRENCH);
        return f.format(cal.getTime());
    }

    /**
     * Renvoit l'heure courante formattée en fonction de la locale
     *
     * @param locale
     *            le format selon le format de la locale
     * @return l'heure courante formattée
     */
    public static String getCurrentTime(Locale locale) {
        return DateUtils.getCurrentTime(Calendar.getInstance(locale));
    }

    /**
     * Renvoit l'heure courante
     *
     * @param locale
     *            format de la Locale
     * @param simpleDateFormat
     *            format de l'heure (cf : java.text.SimpleDateFormat)
     * @return l'heure courante
     */
    public static String getCurrentTime(Locale locale, int simpleDateFormat) {
        Calendar cal = Calendar.getInstance();
        DateFormat f = DateFormat.getTimeInstance(simpleDateFormat, locale);
        return f.format(cal.getTime());
    }

    /**
     * Renvoit l'heure courante
     *
     * @return l'heure au format HH:MM:SS
     */
    public static String getCurrentTimeHMS() {
        Calendar cal = Calendar.getInstance();
        return StringUtils.padBeforeString("" + cal.get(Calendar.HOUR_OF_DAY), "0", 2) + ":"
                + StringUtils.padBeforeString("" + cal.get(Calendar.MINUTE), "0", 2) + ":"
                + StringUtils.padBeforeString("" + cal.get(Calendar.SECOND), "0", 2);
    }

    /**
     * Renvoit la date à partir d'un Calendar
     *
     * @param c
     *            le calendrier
     * @return la date au format AAAAMMJJ
     */
    public static String getDateFromCalendar(Calendar c) {
        String year = "" + c.get(Calendar.YEAR);
        String month = "" + (c.get(Calendar.MONTH) + 1);
        month = StringUtils.padBeforeString(month, "0", 2);
        String day = "" + c.get(Calendar.DAY_OF_MONTH);
        day = StringUtils.padBeforeString(day, "0", 2);
        return year + month + day;
    }

    /**
     * Renvoit la date courante au format JJ.MM.AAAA
     *
     * @return la date courante
     */
    public static String getDateJJMMAAAA_Dots() {
        return DateUtils.convertDate(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ, DateUtils.JJMMAAAA_DOTS);
    }

    /**
     * Renvoit la date d'aujourd'hui plus X jours
     *
     * @param nbreDay
     *            le nombre de jours à rajouter
     * @return La date au format AAAAMMJJ
     */
    public static String getDateToCurrentAMJ(int nbreDay) {
        String returnString = "";
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.add(java.util.Calendar.DAY_OF_MONTH, nbreDay);
        returnString += c.get(java.util.Calendar.YEAR);
        if ((c.get(java.util.Calendar.MONTH) + 1) < 10) {
            returnString += "0" + (c.get(java.util.Calendar.MONTH) + 1);
        } else {
            returnString += c.get(java.util.Calendar.MONTH) + 1;
        }
        if ((c.get(java.util.Calendar.DAY_OF_MONTH)) < 10) {
            returnString += "0" + (c.get(java.util.Calendar.DAY_OF_MONTH));
        } else {
            returnString += c.get(java.util.Calendar.DAY_OF_MONTH);
        }
        return returnString;
    }

    /**
     * Ajoute un nombre X de jours à une date formattée comme on veux
     *
     * @param date
     *            La date de départ
     * @param fromFormat
     *            le format de la date de départ (cf constantes)
     * @param index
     *            le nombre de jours à rajouter
     * @return la date, au même format, avec X jours de plus
     */
    public static String getDateWithDayIndex(String date, String fromFormat, int index) {
        String returnString = "";
        java.util.Calendar c = DateUtils.getCalendarFromDate(date, fromFormat);
        c.add(java.util.Calendar.DAY_OF_MONTH, index);
        returnString += c.get(java.util.Calendar.YEAR);
        if ((c.get(java.util.Calendar.MONTH) + 1) < 10) {
            returnString += "0" + (c.get(java.util.Calendar.MONTH) + 1);
        } else {
            returnString += c.get(java.util.Calendar.MONTH) + 1;
        }
        if ((c.get(java.util.Calendar.DAY_OF_MONTH)) < 10) {
            returnString += "0" + (c.get(java.util.Calendar.DAY_OF_MONTH));
        } else {
            returnString += c.get(java.util.Calendar.DAY_OF_MONTH);
        }
        return returnString;
    }

    /**
     * Renvoit la date et l'heure formatté<br>
     * selon la locale Locale.GERMANY
     *
     * @return la date est l'heure : 10.05.05_15h49
     */
    public static String getLocaleDateAndTime() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.GERMANY);
        String now = df.format(new Date());
        now = now.replace(' ', '_');
        now = now.replace(':', 'h');
        now = now.replace('/', '_');
        return now;
    }

    /**
     * Renvoit le mois et l'année de la date courante
     *
     * @return la date au format MM.AAAA
     */
    public static String getMonthYear() {
        return DateUtils.convertDate(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ, DateUtils.MMAAAA_DOTS);
    }

    /**
     * Renvoit un horodatage lisible
     *
     * @return l'horodatage au format "JJ.MM.AAAA HH:MM:SS : "
     */
    public static String getTimeStamp() {
        return DateUtils.getDateJJMMAAAA_Dots() + " " + DateUtils.getCurrentTimeHMS() + " : ";
    }

    /**
     * Renvoit la date écrite selon la langue spécifiée :<br>
     * ex : 10 mai 2005 10. Mai 2005 10 maggio 2005
     *
     * @param langue
     *            La langue courante (cf:constantes)
     * @param date
     *            la date à écrire
     * @param format
     *            le format de la date (cf:constantes)
     * @return La date en toutes lettres
     */
    public static String getWrittenDate(String langue, String date, String format) {
        Locale currentLocale = null;
        if (DateUtils.LANGUE_DE.equalsIgnoreCase(langue)) {
            currentLocale = Locale.GERMAN;
        } else if (DateUtils.LANGUE_FR.equalsIgnoreCase(langue)) {
            currentLocale = Locale.FRENCH;
        } else if (DateUtils.LANGUE_IT.equalsIgnoreCase(langue)) {
            currentLocale = Locale.ITALIAN;
        } else {
            return date;
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, currentLocale);
        Calendar c = DateUtils.getCalendarFromDate(date, format);
        return df.format(c.getTime());
    }

    /**
     * Renvoit l'année en fonction d'une date
     *
     * @param date
     *            La date
     * @param fromFormat
     *            Le format de la date (cf : constantes)
     * @return L'année
     * @throws ParseException
     *             si la date est invalide ou le format correspond pas
     */
    public static String getYear(String date, String fromFormat) throws ParseException {

        SimpleDateFormat from = new SimpleDateFormat(fromFormat);
        from.setLenient(false);
        Date d = from.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return "" + cal.get(Calendar.YEAR);

    }

    /**
     * Teste si la date est située 17 ans revolus (+1 jour) en arrière
     *
     * @param date
     *            La date au format JJ.MM.AAAA
     * @return true si c'est y'a plus de 17 ans révolus, sinon false
     * @throws Exception
     *             Si la date est invalide ou mal formattée
     */
    public static boolean is17YearsAgo(String date) throws Exception {
        // la date d'aujourd'hui
        Calendar today = Calendar.getInstance();
        // la date à partir de JJ.MM.AAAA
        SimpleDateFormat from = new SimpleDateFormat(DateUtils.JJMMAAAA_DOTS);
        from.setLenient(false);
        Date d;
        try {
            d = from.parse(date);
        } catch (ParseException e) {
            throw e;
        }
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(d);
        return (today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)) >= 17;
    }

    public static boolean is17YearsAgoDateNaissanceIncomplete(int anneeNaissance) throws Exception {
        // la date d'aujourd'hui
        Calendar today = Calendar.getInstance();
        // la date à partir de JJ.MM.AAAA

        return (today.get(Calendar.YEAR) - anneeNaissance) >= 17;
    }

    /**
     * Teste le format d'une date
     *
     * @param value
     *            La date courante
     * @param format
     *            Le format testé
     * @return true si c'est ce format, sinon false
     */
    public static boolean isFormat(String value, String format) {
        try {
            SimpleDateFormat from = new SimpleDateFormat(format);
            from.setLenient(false);
            from.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Constructeur par défaut
     */
    public DateUtils() {
        super();
    }

}
