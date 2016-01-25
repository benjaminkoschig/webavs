package globaz.hermes.utils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Insérez la description du type ici. Date de création : (21.11.2002 08:37:02)
 * 
 * @author: ado
 */
public class HELocalParser {
    //
    public final static int DATE_FORMAT_YYYYMMJJ = 0;
    // allemand
    public final static int DE = 1;
    //
    public final static int EN = 3;
    // french
    public final static int FR = 0;
    //
    public final static int IT = 2;

    /**
     * Insérez la description de la méthode ici. Date de création : (21.11.2002 09:36:57)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        { // en français
            HELocalParser localParse = new HELocalParser(HELocalParser.FR);
            System.out.println("\nLes jours de la semaine en français sont : ");
            for (int i = 0; i <= 7; i++) {
                System.out.println(i + "-" + localParse.getDayOfWeek(i));
            }
            System.out.println("Afficher 10'000,98 francs : " + localParse.parseMoney(10000.98));
            System.out.println("Afficher la date et l'heure : " + localParse.parseDate(System.currentTimeMillis())
                    + ", " + HELocalParser.parseTime(System.currentTimeMillis()));
        }
        { // en allemand
            HELocalParser localParse = new HELocalParser(HELocalParser.DE);
            System.out.println("\nLes jours de la semaine en allemand sont : ");
            for (int i = 0; i <= 7; i++) {
                System.out.println(i + "-" + localParse.getDayOfWeek(i));
            }
            System.out.println("Afficher 10'000,98 francs : " + localParse.parseMoney(10000.98));
            System.out.println("Afficher la date et l'heure : " + localParse.parseDate(System.currentTimeMillis())
                    + ", " + HELocalParser.parseTime(System.currentTimeMillis()));
        }
        { // en italien
            HELocalParser localParse = new HELocalParser(HELocalParser.IT);
            System.out.println("\nLes jours de la semaine en italien sont : ");
            for (int i = 0; i <= 7; i++) {
                System.out.println(i + "-" + localParse.getDayOfWeek(i));
            }
            System.out.println("Afficher 10'000,98 francs : " + localParse.parseMoney(10000.98));
            System.out.println("Afficher la date et l'heure : " + localParse.parseDate(System.currentTimeMillis())
                    + ", " + HELocalParser.parseTime(System.currentTimeMillis()));
        }
        { // en anglais
            HELocalParser localParse = new HELocalParser(HELocalParser.EN);
            System.out.println("\nLes jours de la semaine en anglais sont : ");
            for (int i = 0; i <= 7; i++) {
                System.out.println(i + "-" + localParse.getDayOfWeek(i));
            }
            System.out.println("Afficher 10'000,98 francs : " + localParse.parseMoney(10000.98));
            System.out.println("Afficher la date et l'heure : " + localParse.parseDate(System.currentTimeMillis())
                    + ", " + HELocalParser.parseTime(System.currentTimeMillis()));
        }
    }

    /**
     * s as milliseconds
     * 
     * @return YYYYMMJJ
     */
    public static String parseDate(long s, int format) {
        Calendar staticCalendar = Calendar.getInstance();
        staticCalendar.setTime(new Date(s));
        switch (format) {
            case HELocalParser.DATE_FORMAT_YYYYMMJJ: {
                return "" + staticCalendar.get(Calendar.YEAR) + (staticCalendar.get(Calendar.MONTH) + 1)
                        + staticCalendar.get(Calendar.DAY_OF_MONTH);
            }
            default: // par défaut AAAAMMJJ
            {
                return "" + staticCalendar.get(Calendar.YEAR) + (staticCalendar.get(Calendar.MONTH) + 1)
                        + staticCalendar.get(Calendar.DAY_OF_MONTH);
            }
        }
    }

    /**
     * return as HH:MM:SS (SHORT dateFormat)
     * 
     * @return time
     */
    public static String parseTime(long currentTimeMillis) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        return df.format(new Date(currentTimeMillis));
    }

    // calendar
    private Calendar calendar;

    // country
    private Locale countryLocale;

    // language
    private Locale languageLocale;

    /**
     * Commentaire relatif au constructeur HECalendar.
     */
    public HELocalParser(int country) {
        calendar = Calendar.getInstance();
        setLocale(country);
    }

    /**
     * Returns day of week according to language
     * 
     * @param int day of week, 0 is blank, sunday is 1, monday is 2... saturday is 7
     */
    public String getDayOfWeek(int oneBasedDay) {
        DateFormatSymbols dfs = new DateFormatSymbols(countryLocale);
        return dfs.getWeekdays()[oneBasedDay];
    }

    /**
     * s as milliseconds
     * 
     * @return JJ/MM/YYYY
     */
    public String parseDate(long s) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, countryLocale);
        return df.format(new Date(s));
    }

    //
    public String parseMoney(double amount) {
        return NumberFormat.getCurrencyInstance(countryLocale).format(amount);
    }

    public void setLocale(int country) {
        switch (country) {
            case HELocalParser.FR: {
                countryLocale = Locale.FRANCE;
                languageLocale = Locale.FRENCH;
                break;
            }
            case HELocalParser.DE: {
                countryLocale = Locale.GERMANY;
                languageLocale = Locale.GERMAN;
                break;
            }
            case HELocalParser.IT: {
                countryLocale = Locale.ITALY;
                languageLocale = Locale.ITALIAN;
                break;
            }
            case HELocalParser.EN: {
                countryLocale = Locale.UK;
                languageLocale = Locale.ENGLISH;
                break;
            }
            default: {
                countryLocale = Locale.FRANCE;
                languageLocale = Locale.FRENCH;
            }
        }
    }
}
