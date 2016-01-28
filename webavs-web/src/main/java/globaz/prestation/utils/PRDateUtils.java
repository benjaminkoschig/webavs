package globaz.prestation.utils;

import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.beans.PRPeriode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Classe utilitaire pour effectuer des opérations sur les périodes et les dates
 * 
 * @author lga
 */
public class PRDateUtils {

    public enum PRDateEquality {
        AFTER,
        BEFORE,
        EQUALS,
        INCOMPARABLE;
    }

    public enum DateOrder {
        NEWER_TO_OLDER,
        OLDER_TO_NEWER;
    }

    public class DateFormatException extends Exception {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public DateFormatException(String message) {
            super(message);
        }
    }

    /**
     * Compare la date1 avec la date2. </br> 1 - Test si les 2 dates fournies sont des GlobazDate, si ce n'est pas le
     * cas, une Exception sera lancée.</br> 2 - Test que les 2 chaînes de caractères soient de taille identique sinon
     * exception.</br> 3 - Effectue les test temporel, la date1 est la référence ce qui veut dire que date2 sera évalué
     * par rapport à date1.</br> Exemple : date1 = 20.04.2012, date2 = 21.04.2012 return AFTER
     * 
     * @see Test case : REDateUtilsTest.compare()
     * @param date1
     *            La date de référence
     * @param date2
     *            La date à évaluer
     * @return
     * @throws Exception
     *             Si les formats des dates sont incorrects
     */
    public static PRDateEquality compare(String date1, String date2) {
        if (!JadeDateUtil.isGlobazDate(date1)) {
            return PRDateEquality.INCOMPARABLE;
        }
        if (!JadeDateUtil.isGlobazDate(date2)) {
            return PRDateEquality.INCOMPARABLE;
        }
        if (date1.length() != date2.length()) {
            return PRDateEquality.INCOMPARABLE;
        }
        if (JadeDateUtil.isDateBefore(date1, date2)) {
            return PRDateEquality.AFTER;
        } else if (JadeDateUtil.isDateAfter(date1, date2)) {
            return PRDateEquality.BEFORE;
        } else {
            return PRDateEquality.EQUALS;
        }
    }

    /**
     * Trie une liste de dates sous forme de String. Les date doivent être au format Globaz date sinon une exception
     * sera lancée
     * 
     * @param dates
     *            La liste de dates à trier
     * @param order
     *            L'ordre de tri
     * @return <strong>Une nouvelle liste</strong> contenant les dates triées dans l'ordre définit par le paramètre
     *         <code>order>/code>
     * @throws Exception
     *             Si dates ou order sont null, ou si les dates n'ont pas un format Globaz valides
     */
    public static List<String> sortDate(List<String> dates, DateOrder order) throws Exception {
        if (dates == null) {
            throw new Exception("The dates list is null");
        }
        if ((order == null)) {
            throw new Exception("The order parameter is null");
        }
        if (dates.size() == 0) {
            return dates;
        }
        for (int ctr = 0; ctr < dates.size(); ctr++) {
            if (!JadeDateUtil.isGlobazDate(dates.get(ctr))) {
                throw new Exception("The date number " + ctr + " has un invalid date format [" + dates.get(ctr)
                        + "]. The date format must be dd.MM.yyyy dd.MM.yy");
            }
            if (dates.get(ctr).length() != 10) {
                throw new Exception("The date length is wrong for date [" + dates.get(ctr)
                        + "]. Maybee it has a wrong format, the date format must be dd.MM.yyyy dd.MM.yy");
            }
        }

        List<Calendar> list = new ArrayList<Calendar>();
        Calendar date = null;
        for (int ctr = 0; ctr < dates.size(); ctr++) {
            date = PRDateUtils.convertGlobazDateToJavaDate(dates.get(ctr));
            list.add(date);
        }
        Collections.sort(list);

        List<String> result = new ArrayList<String>();
        String stringDate = null;
        for (int ctr = 0; ctr < list.size(); ctr++) {
            date = list.get(ctr);
            stringDate = PRDateUtils.convertCalendarToGlobazDate(date);
            result.add(stringDate);
        }
        if (order.equals(DateOrder.OLDER_TO_NEWER)) {
            Collections.reverse(result);
        }
        return result;
    }

    /**
     * @param date
     * @return
     */
    public static String convertCalendarToGlobazDate(Calendar date) {
        StringBuilder sb;
        sb = new StringBuilder();
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(dayOfMonth));
        sb.append(".");

        int month = date.get(Calendar.MONTH);
        month++;
        if (month < 10) {
            sb.append("0");
        }

        sb.append(String.valueOf(month));
        sb.append(".");
        sb.append(String.valueOf(date.get(Calendar.YEAR)));
        return sb.toString();
    }

    /**
     * @param dates
     * @param ctr
     * @return
     * @throws Exception
     */
    public static Calendar convertGlobazDateToJavaDate(String value) throws Exception {
        if (!JadeDateUtil.isGlobazDate(value)) {
            throw new Exception("The provided String date has a wrong format [" + value + "]");
        }
        String[] values;

        Calendar c = Calendar.getInstance();
        values = value.split("\\.");
        if (values.length != 3) {
            throw new Exception("Can not split the date [" + value
                    + "]. Maybee it has a wrong format, the date format must be dd.MM.yyyy or dd.MM.yy");
        }
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(values[0]));
        c.set(Calendar.MONTH, Integer.parseInt(values[1]) - 1);
        c.set(Calendar.YEAR, Integer.parseInt(values[2]));
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * Test si la da est contenue dans la période</br> <strong>Condition : </strong></br> - la date de début doit être
     * égal ou inférieur (située avant dans le temps) à la date</br> - la date de fin doit être égal ou supérieur
     * (située après dans le temps) à la date</br>
     * 
     * @param periode
     *            La période de référence
     * @param date
     *            La date à évaluer
     * @return <code>true</code> si la date est comprise dans la période
     * @throws Exception
     *             Si la date n'est pas valide, si la période n'est pas valide ou si les dates de la période sont
     *             invalides
     */
    public static boolean isDateDansLaPeriode(PRPeriode periode, String date) throws IllegalArgumentException {
        if (periode == null) {
            throw new IllegalArgumentException("The PRPeriode is null");
        }
        if (!JadeDateUtil.isGlobazDate(periode.getDateDeDebut())) {
            throw new IllegalArgumentException("The PRPeriode.dateDeDebut is not a valid Globaz Date format");
        }
        if (!JadeDateUtil.isGlobazDate(periode.getDateDeFin())) {
            throw new IllegalArgumentException("The PRPeriode.dateDeDebut is not a valid Globaz Date format");
        }
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new IllegalArgumentException("The date is not a valid Globaz Date format");
        }

        boolean r1 = false;
        boolean r2 = false;
        // Test 1 : la date de début doit être égal ou inférieur (BEFORE) à la date
        switch (PRDateUtils.compare(date, periode.getDateDeDebut())) {
            case EQUALS:
            case BEFORE:
                r1 = true;
                break;
            default:
                break;
        }

        // Test 2 : la date de fin doit être égal ou supérieur (AFTER) à la date
        switch (PRDateUtils.compare(date, periode.getDateDeFin())) {
            case EQUALS:
            case AFTER:
                r2 = true;
                break;
            default:
                break;
        }
        return r1 && r2;
    }

    /**
     * Contrôle si des chevauchement de dates existent dans un ensemble de périodes
     * 
     * @param periodes
     *            Liste des périodes à analyser
     * @return <code>true</code> si un chevauchement est détecté
     */
    public static boolean hasChevauchementDePeriodes(List<PRPeriode> periodes) {
        boolean chevauchement = false;
        int ctr1 = 0;
        int ctr2 = 0;
        do {
            do {
                // On ne veut pas comparer les mêmes périodes
                if (ctr1 != ctr2) {
                    PRPeriode periodeReference = periodes.get(ctr1);
                    PRPeriode periodeTest = periodes.get(ctr2);
                    if (PRDateUtils.isDateDansLaPeriode(periodeReference, periodeTest.getDateDeDebut())) {
                        chevauchement = true;
                    }
                    if (PRDateUtils.isDateDansLaPeriode(periodeReference, periodeTest.getDateDeFin())) {
                        chevauchement = true;
                    }
                }
                ctr2++;
            } while (!chevauchement && (ctr2 < periodes.size()));
            ctr1++;
            ctr2 = ctr1 + 1;
        } while (!chevauchement && (ctr2 < periodes.size()) && (ctr1 < periodes.size()));
        return chevauchement;
    }

    /**
     * Retourne le nombre de jours entre deux 'Globaz date'. La date de fin doit être égale ou plus grande que la date
     * de début.</br> Cette méthode est sous tests unitaires et fonctionne... </br><strong>Le dernier jours n'est pas
     * compté ! </strong></br> Exemple : 01.03.2013 -> 31.03.2013 = 30 jours
     * 
     * @param date1
     *            La date de début, doit avoir un format Globaz date valide (dd.MM.yy ou dd.MM.yyyy)
     * @param date2
     *            La date de fin, doit avoir un format Globaz date valide (dd.MM.yy ou dd.MM.yyyy)
     * @return Le nombre de jours entre ces 2 dates
     * @throws IllegalArgumentException
     *             Si <code>dateDeDebut</code> ou la <code>dateDeFin</code> sont null ou n'ont pas le bon format OU si
     *             <code>dateDeDebut</code> est plus grand que <code>dateDeFin</code>
     */
    public static int getNbDayBetween(String dateDeDebut, String dateDeFin) throws IllegalArgumentException {
        PRDateUtils.validateDateDebutAndDateFin(dateDeDebut, dateDeFin);
        if (JadeDateUtil.areDatesEquals(dateDeDebut, dateDeFin)) {
            return 0;
        }

        int ctr = 0;
        GregorianCalendar nextDate = PRDateUtils.convertStringToCalendar(dateDeDebut);
        GregorianCalendar stopDate = PRDateUtils.convertStringToCalendar(dateDeFin);

        while (!stopDate.equals(nextDate)) {
            ctr++;
            nextDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return ctr;
    }

    /**
     * Retourne le nombre de jours entre deux 'Globaz date'. La date de fin doit être égale ou plus grande que la date
     * de début.</br> Cette méthode est sous tests unitaires et fonctionne... </br><strong>Le dernier jours n'est pas
     * compté ! </strong></br> Exemple : 01.03.2013 -> 31.03.2013 = 30 jours
     * Cette méthode est quasiment identique à la méthode {@link PRDateUtils.getNbDayBetween()}, seule la manière de
     * calculer le
     * nombre de jours diffère
     * 
     * @param date1
     *            La date de début, doit avoir un format Globaz date valide (dd.MM.yy ou dd.MM.yyyy)
     * @param date2
     *            La date de fin, doit avoir un format Globaz date valide (dd.MM.yy ou dd.MM.yyyy)
     * @return Le nombre de jours entre ces 2 dates
     * @throws IllegalArgumentException
     *             Si <code>dateDeDebut</code> ou la <code>dateDeFin</code> sont null ou n'ont pas le bon format OU si
     *             <code>dateDeDebut</code> est plus grand que <code>dateDeFin</code>
     */
    public static int getNbDayBetween2(String dateDeDebut, String dateDeFin) {
        PRDateUtils.validateDateDebutAndDateFin(dateDeDebut, dateDeFin);
        if (JadeDateUtil.areDatesEquals(dateDeDebut, dateDeFin)) {
            return 0;
        }

        Date dateBefore = JadeDateUtil.getGlobazDate(dateDeDebut);
        Date dateAfter = JadeDateUtil.getGlobazDate(dateDeFin);

        if ((dateBefore != null) && (dateAfter != null) && (dateAfter.getTime() >= dateBefore.getTime())) {
            double msDiff = dateAfter.getTime() - dateBefore.getTime();
            return (int) Math.round(msDiff / (1000 * 60 * 60 * 24));
        }
        return -1;
    }

    /**
     * Valide le format de <code>dateDeDebut</code> et <code>dateDeFin</code>. <strong>Le format doit correspondre à
     * l'un des deux pattern suivants : (dd.MM.yy ou dd.MM.yyyy)</strong></br>
     * <strong>Valide également que la date de début ne soit pas plus grande que la date de fin.</strong></br>
     * 
     * @param dateDeDebut La date de début au format (dd.MM.yy ou dd.MM.yyyy)
     * @param dateDeFin La date de fin au format (dd.MM.yy ou dd.MM.yyyy)
     */
    private static void validateDateDebutAndDateFin(String dateDeDebut, String dateDeFin) {
        if (!JadeDateUtil.isGlobazDate(dateDeDebut)) {
            throw new IllegalArgumentException(
                    "PRDateUtils.validateDateDebutAndDateFin(dateDeDebut, dateDeFin) : the 'dateDeDebut' does not have a valid Globaz format ["
                            + dateDeDebut + "]");
        }
        if (!JadeDateUtil.isGlobazDate(dateDeFin)) {
            throw new IllegalArgumentException(
                    "PRDateUtils.validateDateDebutAndDateFin(dateDeDebut, dateDeFin) : the 'dateDeFin' does not have a valid Globaz format ["
                            + dateDeFin + "]");
        }
        if (dateDeDebut.length() != dateDeFin.length()) {
            throw new IllegalArgumentException(
                    "PRDateUtils.validateDateDebutAndDateFin(dateDeDebut, dateDeFin) : the 'dateDeDebut' ["
                            + dateDeDebut + "] does not have the same format like the 'dateDeFin' [" + dateDeFin + "]");
        }

        SimpleDateFormat toDateFormatter = new SimpleDateFormat((dateDeDebut.length() == 10) ? "dd.MM.yyyy"
                : "dd.MM.yy");
        SimpleDateFormat toIntegerFormatter = new SimpleDateFormat("yyyyMMdd");
        int date1, date2 = 0;
        try {
            date1 = Integer.valueOf(toIntegerFormatter.format(toDateFormatter.parse(dateDeDebut)));
            date2 = Integer.valueOf(toIntegerFormatter.format(toDateFormatter.parse(dateDeFin)));
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "PRDateUtils.validateDateDebutAndDateFin(dateDeDebut, dateDeFin) : Error when parsing 'dateDeDebut' ["
                            + dateDeDebut + "] or 'dateDeFin' [" + dateDeFin + "]");
        }

        if (date2 < date1) {
            throw new IllegalArgumentException(
                    "PRDateUtils.getNbDayBetween(dateDeDebut, dateDeFin) : the 'dateDeDebut' must be the smaller date. dateDeDebut ["
                            + dateDeDebut + "], dateDeFin [" + dateDeFin + "]");
        }
    }

    private static String calendarToString(GregorianCalendar calendar) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(calendar.get(Calendar.DAY_OF_MONTH));
        sb.append(".");
        sb.append(calendar.get(Calendar.MONTH) + 1);
        sb.append(".");
        sb.append(calendar.get(Calendar.YEAR));
        sb.append(".");
        sb.append("]");
        return sb.toString();
    }

    private static GregorianCalendar convertStringToCalendar(String date) {
        String[] array = date.split("\\.");
        if (array.length != 3) {
            throw new RuntimeException("Error on splitting date : " + date);
        }
        return new GregorianCalendar(Integer.valueOf(array[2]), Integer.valueOf(array[1]) - 1,
                Integer.valueOf(array[0]));
    }

    /**
     * Retourne l'age d'une personne à la date du jour en fonction de sa date de naissance
     * 
     * @param dateDeNaissance
     *            La date de naissance au format JJ.MM.AAAA
     * @return
     */
    public static int getAge(String dateDeNaissance) throws DateException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return PRDateUtils.getAge(dateDeNaissance, format.format(new Date()));
    }

    /**
     * Retourne l'âge du tiers à la date voulue
     * 
     * @param dateDeNaissance
     *            La date de naissance du tiers au format JJ.MM.AAAA
     * @param dateVoulue
     *            La date à laquelle doit être calculé l'âge du tiers au format JJ.MM.AAAA. Doit être situé après la
     *            dateDeNaissance
     * @throws DateException
     *             En cas de format de date invalid ou si la dateVoulue est située avant la date de naissance
     * @return L'âge du tiers à la date voulue
     */
    public static int getAge(String dateDeNaissance, String dateVoulue) throws DateException {
        if (!JadeDateUtil.isGlobazDate(dateDeNaissance)) {
            throw new DateException("the provided dateDeNaissance [" + dateDeNaissance + "] is not a valid date");
        }
        if (!JadeDateUtil.isGlobazDate(dateDeNaissance)) {
            throw new DateException("the provided dateVoulue [" + dateVoulue + "] is not a valid date");
        }
        if (PRDateUtils.compare(dateDeNaissance, dateVoulue).equals(PRDateEquality.BEFORE)) {
            throw new DateException("the dateVoulue [" + dateVoulue + "] is before the dateDeNaissance ["
                    + dateDeNaissance + "]");
        }
        if (PRDateUtils.compare(dateDeNaissance, dateVoulue).equals(PRDateEquality.INCOMPARABLE)) {
            throw new DateException("Error occur, dates can not be compared; dateVoulue [" + dateVoulue
                    + "], dateDeNaissance [" + dateDeNaissance + "]");
        }
        int age = 0;
        try {
            Calendar dateDeNaissanceCalendar = PRDateUtils.convertGlobazDateToJavaDate(dateDeNaissance);
            Calendar dateVoulueCalendar = PRDateUtils.convertGlobazDateToJavaDate(dateVoulue);
            age = dateVoulueCalendar.get(Calendar.YEAR) - dateDeNaissanceCalendar.get(Calendar.YEAR);
            // Important si l'age est égal à 0, on ne pouruit pas l'analyse plus loin
            if (age == 0) {
                return age;
            }

            age--;
            // si le mois voulut est plus grand c'est qu'il à déjà eu son anniversaire
            if (dateVoulueCalendar.get(Calendar.MONTH) > dateDeNaissanceCalendar.get(Calendar.MONTH)) {
                age++;
            }
            // Si les mois ont identique, il faut contrôler les jours sont supérieurs ou égaux
            else if (dateVoulueCalendar.get(Calendar.MONTH) == dateDeNaissanceCalendar.get(Calendar.MONTH)) {
                // Si le jour de la date voulue est plus grand ou égal au jour de son anniversaire
                if (dateVoulueCalendar.get(Calendar.DAY_OF_MONTH) >= dateDeNaissanceCalendar.get(Calendar.DAY_OF_MONTH)) {
                    // c'est qu'il à, ou à déjà eu son anniversaire donc +1
                    age++;
                }
            }
        } catch (Exception e) {
            throw new DateException("An exception was thrown during the analysis of the age of the person", e);
        }
        return age;
    }
}
