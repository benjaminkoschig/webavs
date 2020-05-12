package ch.globaz.al.utils;

import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.exceptions.utils.ALUtilsException;

/**
 * Fournit des méthodes de gestion de dates
 * 
 * @author jts/pta
 */
public abstract class ALDateUtils {

    /**
     * constante pour l'ajout jour-mois pour début période annuelle (premier jour du premier mois)
     */
    public static final String JOUR_MOIS_ANNEE_DEB = "01.01.";

    /**
     * constante pour l'ajout jour-mois pour fin période annuelle (dernier jour du dernier mois de l'année)
     */
    public static final String JOUR_MOIS_ANNEE_FIN = "31.12.";

    /**
     * constante pour l'ajout du début du mois pour période mensuelle (premier)
     */
    public static final String JOUR_PREMIER_MOIS = "01.";

    public static final Integer FIN_AGE_DROIT_ENFANT = 16;

    /**
     * Méthode permettant d'ajouter des années à une date
     * 
     * @param annees
     *            nombre d'années à ajouter (pour soustraire, passer un nombre négatif)
     * @param date
     *            date (format JJ.MM.AAAA)
     * @return date à laquelle <code>annees</code> ont été ajoutées
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètre n'est pas valide ou si la date n'a pas pu être parsée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static Calendar addAnneesDate(int annees, Calendar date) throws JadeApplicationException {

        if (date == null) {
            throw new ALUtilsException("ALDateUtils#ajoutAnneesDate :" + date + " is not a valid date");
        }

        date.add(Calendar.YEAR, annees);
        return date;
    }

    /**
     * Méthode permettant d'ajouter des jours à une date
     * 
     * @param jour
     *            nombre de jour à ajouter (pour soustraire, passer un nombre négatif)
     * @param date
     *            date (format JJ.MM.AAAA)
     * @return date à laquelle <code>mois</code> ont été ajoutées
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètre n'est pas valide ou si la date n'a pas pu être parsée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static Calendar addJourDate(int jour, Calendar date) throws JadeApplicationException {
        if (date == null) {
            throw new ALUtilsException("ALDateUtils@retraitJourDate :" + date + "is not a valid date");
        }

        date.add(Calendar.DATE, jour);
        return date;
    }

    /**
     * Méthode permettant d'ajouter des mois à une date
     * 
     * @param mois
     *            nombre de mois à ajouter (pour soustraire, passer un nombre négatif)
     * @param date
     *            date (format JJ.MM.AAAA)
     * @return date à laquelle <code>mois</code> ont été ajoutées
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètre n'est pas valide ou si la date n'a pas pu être parsée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static Calendar addMoisDate(int mois, Calendar date) throws JadeApplicationException {
        if (date == null) {
            throw new ALUtilsException("ALDateUtils@addMoisDate :" + date + "is not a valid date");
        }

        date.add(Calendar.MONTH, mois);
        return date;
    }

    /**
     * Formate une date en GlobazFormatedDate (jj.mm.aaaa)
     * 
     * @param date
     *            La date à formater
     * @param format
     *            Le format actuel de la date formaté
     * @return La date formatée
     * @throws ParseException
     */
    public static String convertDate(String date, String format) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return JadeDateUtil.getGlobazFormattedDate(formater.parse(date));
    }

    /**
     * Énumère chaque période se trouvant entre beginInterval et endInterval (inclus) et les stocke dans une liste
     * 
     * @param beginInterval
     *            mm.YYYY
     * @param endInterval
     *            mm.YYYY
     * @return ArrayList La liste sous forme [01.2009;02.2009;...;12.2009]
     * @throws JadeApplicationException
     *             Exception levée si un paramètre n'est pas correct
     */
    public static ArrayList<String> enumPeriodeFromInterval(String beginInterval, String endInterval)
            throws JadeApplicationException {

        ArrayList<String> periodeList = new ArrayList<String>();

        if (!JadeDateUtil.isGlobazDateMonthYear(beginInterval)) {
            throw new ALUtilsException("ALDateUtils#enumPeriodeFromInterval :" + beginInterval
                    + " is not a valid date (mm.YYYY)");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(endInterval)) {
            throw new ALUtilsException("ALDateUtils#enumPeriodeFromInterval :" + endInterval
                    + " is not a valid date (mm.YYYY)");
        }

        if (!JadeDateUtil.areDatesEquals("01.".concat(beginInterval), "01.".concat(endInterval))
                && !JadeDateUtil.isDateBefore("01.".concat(beginInterval), "01.".concat(endInterval))) {
            throw new ALUtilsException("ALDateUtils#enumPeriodeFromInterval :beginInterval must be <= endInterval");
        }

        // Remplissage de la hashmap avec 1 clé par période, on affectera les
        // valeurs dans l'arraylist ensuite
        String newPeriodeDate = "01.".concat(beginInterval);
        do {

            periodeList.add(newPeriodeDate.substring(3));
            newPeriodeDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                    ALDateUtils.getCalendarDate(newPeriodeDate)).getTime());

        } while (!JadeDateUtil.isDateAfter(newPeriodeDate, "01.".concat(endInterval)));

        return periodeList;

    }

    /**
     * Parse <code>date</code> en <code>Calendar</code>
     * 
     * @param date
     *            Date à parser
     * @return La <code>date</code> parsée en <code>Calendar</code>
     * 
     * @throws JadeApplicationException
     *             Exception levée si la <code>date</code> n'est pas valide ou si elle n'a pas pu être parsée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static Calendar getCalendarDate(String date) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALUtilsException("ALDateUtils#getCalendarDate :" + date + " is not a valid date");
        }

        Calendar dateCal = Calendar.getInstance();

        try {
            dateCal.setTime(date.length() == 10 ? new SimpleDateFormat("dd.MM.yyyy").parse(date)
                    : new SimpleDateFormat("dd.MM.yy").parse(date));
            return dateCal;
        } catch (ParseException e) {
            throw new ALUtilsException("ALDateUtils#getCalendarDate : unable to parse date '" + date + "'", e);
        }
    }

    /**
     * Méthode qui retourne une date (String) "dd.MM.yyyy" au dernier du mois,en fonction de la date passée en paramètre
     * et du nombre d'années ajouter à la date
     * 
     * @param date
     *            date à laquelle ajouter <code>nbreAnnees</code>
     * @param nbreAnnees
     *            nombre d'années à ajouter à la <code>date</code>
     * @return date modifiée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static String getDateAjoutAnneesFinMois(String date, int nbreAnnees) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALDroitBusinessException("DatesEcheanceServiceImpl#getDateAjoutAnneesFinMois : " + date
                    + " is not a valid date");
        }

        return JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addAnneesDate(nbreAnnees,
                ALDateUtils.getDateFinMois(ALDateUtils.getCalendarDate(date))).getTime());
    }

    /**
     * Méthode retournant la date courante au format dd.MM.yyyy
     * 
     * @return date courante
     * @deprecated Utiliser JadedateUtil.getGlobazFormattedDate
     */
    @Deprecated
    public static String getDateCourante() {
        String date = null;

        Calendar c = Calendar.getInstance();
        Date dujour = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        date = dateFormat.format(dujour);

        return date;
    }

    /**
     * Méthode qui retourne le début d'une année en dd.mm.yyyy de l'année passée en paramètre
     * 
     * @param annee
     *            année YYYY
     * @return String
     * @throws JadeApplicationException
     *             Exception levée si la périodicité indiquée n'est pas valide
     * 
     */
    public static String getDateDebutAnnee(String annee) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateYear(annee)) {
            throw new ALUtilsException("DateUtils#getDateDebutAnnee" + annee + " is not a valid year'date");
        }

        String dateDebut = null;

        dateDebut = ALDateUtils.JOUR_MOIS_ANNEE_DEB + annee;

        return dateDebut;
    }

    /**
     * Retourne la date du premier jour du mois pour la date passé en paramètres
     * 
     * @param date
     *            Date pour laquelle récupérer le premier jour
     * @return premier jour du mois pour la date passée en paramètre
     * @throws JadeApplicationException
     *             Exception levée si la date indiquée n'est pas valide
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static String getDateDebutMois(String date) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALUtilsException("ALDateUtils#getDateDebutMois :" + date + " is not a valid date");

        }

        return ALDateUtils.getDateDebutMoisPourPeriode(date.substring(3));
    }

    /**
     * Méthode qui retourne le début du mois en dd.mm.yyyy pour une période en mm.yyyy passée en paramètre
     * 
     * @param periode
     *            période en mm.yyyy
     * @return String date dd.mm.yyyy
     * @throws JadeApplicationException
     *             Exception levée si la période indiquée n'est pas valide
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static String getDateDebutMoisPourPeriode(String periode) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALUtilsException("ALDateUtils#getDateDebutMoisPourPeriode" + periode + " is not a valid period");
        }

        return ALDateUtils.JOUR_PREMIER_MOIS + periode;
    }

    /**
     * Méthode qui retourne la date de fin d'année en dd.mm.yyyy pour l'année passée en paramètre
     * 
     * @param annee
     *            YYYY
     * @return String date dd.mm.yyyy
     * @throws JadeApplicationException
     *             Exception levée si la périodicité indiquée n'est pas valide
     */
    public static String getDateFinAnnee(String annee) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateYear(annee)) {
            throw new ALUtilsException("DateUtils#getDateFinAnnee" + annee + " is not a valid year's date");
        }

        return ALDateUtils.JOUR_MOIS_ANNEE_FIN + annee;
    }

    /**
     * Méthode qui met une date à la fin du mois courant et la retourne en date "dd.MM.yyyy" type globaz
     * 
     * @param date
     *            objet Calendar
     * @return dateMois date (String) "dd.MM.yyyy"
     * @throws JadeApplicationException
     *             Exception levée si la <code>date</code> n'est pas valide ou si elle n'a pas pu être parsée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static Calendar getDateFinMois(Calendar date) throws JadeApplicationException {

        if (date == null) {
            throw new ALUtilsException("ALDateUtils#dateFinMois :" + date + " is not a valid date");
        }

        int nbrJourMois = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        date.set(Calendar.DAY_OF_MONTH, nbrJourMois);
        return date;
    }

    /**
     * Retourne la fin d'un mois par rapport à une date de ce même mois
     * 
     * @param date
     *            Une date du mois en chaîne de caractère (format dd.MM.yyyy)
     * @return La fin du mois
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètre n'est pas valide ou si la date n'a pas pu être parsée
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static String getDateFinMois(String date) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALUtilsException("ALDateUtils#getDateFinMois :" + date + " is not a valid date");

        }
        Calendar calendar = Calendar.getInstance();
        Date newDate = null;

        try {
            newDate = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException e) {
            throw new ALUtilsException("ALDateUtils#getCalendarDate : unable to parse" + date, e);
        }

        calendar.setTime(newDate);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return new Integer(day).toString() + date.substring(2);
    }

    /**
     * Méthode qui retourne la fin d'un mois en dd.mm.yyyy pour une période en mm.yyyy passée en paramètre
     * 
     * @param periode
     *            période en mm.yyyy
     * @return String date dd.mm.yyyy
     * 
     * @throws JadeApplicationException
     *             Exception levée si la périodicité indiquée n'est pas valide
     * 
     * @deprecated Utiliser la méthode équivalente existante dans JadeDateUtil
     */
    @Deprecated
    public static String getDateFinMoisPourPeriode(String periode) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(periode)) {
            throw new ALUtilsException("DateUtils#getDateFinMoisPourPeriode" + periode
                    + " is not a valid monthYear's date");
        }

        return ALDateUtils.getDateFinMois(ALDateUtils.getDateDebutMoisPourPeriode(periode));
    }

    /**
     * Détermine et retourne le début de la période en fonction de la fin de la période et de la périodicité (mensuelle,
     * trimestrielle ou annuelle). Dans le cas d'une périodicité trimestrielle, si la fin de période indiquée ne
     * correspond pas à la fin d'un trimestre, le mois de début exact est tout de même retourné (ex : 11.2009 retournera
     * 10.2009)
     * 
     * @param finPeriode
     *            Fin de la période à traité (format MM.AAAA)
     * @param periodicite
     *            périodicité de l'affilié à traiter
     * @return début de la période
     * @throws JadeApplicationException
     *             Exception levée si la périodicité indiquée n'est pas valide ou si <code>finPeriode</code> n'a pas le
     *             format requis
     */
    public static String getDebutPeriode(String finPeriode, String periodicite) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDateMonthYear(finPeriode)) {
            throw new ALUtilsException("ALDateUtils#getDebutPeriode : " + finPeriode + " is not a valid period");
        }

        if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {

            int mois = Integer.parseInt(finPeriode.substring(0, 2));
            int moisdebut = 0;

            if (mois <= 3) {
                moisdebut = 1;
            } else if (mois <= 6) {
                moisdebut = 4;
            } else if (mois <= 9) {
                moisdebut = 7;
            } else {
                moisdebut = 10;
            }

            return (moisdebut < 10 ? "0" + String.valueOf(moisdebut) : String.valueOf(moisdebut)) + "."
                    + finPeriode.substring(3, 7);
        } else if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite)) {
            return "01." + finPeriode.substring(3, 7);
        } else if (ALCSAffilie.PERIODICITE_MEN.equals(periodicite)) {
            return finPeriode;
        } else {
            throw new ALUtilsException("ALDateUtils#getDebutPeriode : " + periodicite + " is not a supported value");
        }
    }

    /**
     * Used to get number of months between two dates (including the first and last month).
     * 
     * @param beforeDate
     *            The start date of the range
     * @param afterDate
     *            The end date of the range
     * @return Number of years between the two dates, <i>-1</i> if :<br>
     *         - beforeDate and afterDate aren't valid dates<br>
     *         - beforeDtate is after the afterDate in the range of dates
     */
    public static int getNbMonthsBetween(String beforeDate, String afterDate) {

        int nb = -1;
        try {
            if (JadeDateUtil.isGlobazDate(beforeDate) && JadeDateUtil.isGlobazDate(afterDate)) {
                if (JadeDateUtil.isDateBefore(beforeDate, afterDate)
                        || JadeDateUtil.areDatesEquals(beforeDate, afterDate)) {
                    Calendar theBeforeDate = Calendar.getInstance();
                    theBeforeDate.setTime(beforeDate.length() == 10 ? new SimpleDateFormat("dd.MM.yyyy")
                            .parse(beforeDate) : new SimpleDateFormat("dd.MM.yy").parse(beforeDate));

                    Calendar theAfterDate = Calendar.getInstance();
                    theAfterDate.setTime(afterDate.length() == 10 ? new SimpleDateFormat("dd.MM.yyyy").parse(afterDate)
                            : new SimpleDateFormat("dd.MM.yy").parse(afterDate));

                    int nbYear = theAfterDate.get(Calendar.YEAR) - theBeforeDate.get(Calendar.YEAR);

                    if (nbYear == 0) {
                        nb = theAfterDate.get(Calendar.MONTH) - theBeforeDate.get(Calendar.MONTH) + 1;
                    } else {
                        nb = 12 - theBeforeDate.get(Calendar.MONTH) + (theAfterDate.get(Calendar.MONTH)) + 1;

                        if (nbYear > 1) {
                            nb += (nbYear - 1) * 12;
                        }
                    }
                }
            }
        } catch (ParseException pe) {
            nb = -1;
        }
        return nb;
    }

    public static XMLGregorianCalendar globazDateToXMLGregorianCalendar(String date) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALUtilsException("ALDateUtils#globazDateToXMLGregorianCalendar : " + date
                    + " is not a valid date");
        }

        String[] split = date.split("\\.");
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendarDate(Integer.parseInt(split[2]),
                    Integer.parseInt(split[1]), Integer.parseInt(split[0]), DatatypeConstants.FIELD_UNDEFINED);
        } catch (NumberFormatException e) {
            throw new ALUtilsException("ALDateUtils#globazDateToXMLGregorianCalendar : unable to parse date " + date, e);
        } catch (DatatypeConfigurationException e) {
            throw new ALUtilsException("ALDateUtils#globazDateToXMLGregorianCalendar : unable to convert date " + date,
                    e);
        }
    }

    /**
     * Vérifie si <code>dateToCheck</code> se trouve entre <code>beginDate</code> et <code>endDate</code>
     * 
     * @param dateToCheck
     *            La date à vérifier
     * @param beginDate
     *            La date de début
     * @param endDate
     *            La date de fin
     * @return <code>true</code> si <code>dateToCheck</code> se trouve entre <code>beginDate</code> et
     *         <code>endDate</code>
     * 
     * @throws JadeApplicationException
     *             Exception levée si l'une des date n'est pas valide ou si <code>endDate</code> est avant
     *             <code>beginDate</code>
     */
    public static boolean isDateBetween(String dateToCheck, String beginDate, String endDate)
            throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(dateToCheck)) {
            throw new ALUtilsException("ALDateUtils#isDateBetween : " + dateToCheck + " is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDate(beginDate)) {
            throw new ALUtilsException("ALDateUtils#isDateBetween : " + beginDate + " is not a valid date");
        }

        if (!JadeDateUtil.isGlobazDate(endDate)) {
            throw new ALUtilsException("ALDateUtils#isDateBetween : " + endDate + " is not a valid date");
        }

        if (JadeDateUtil.isDateBefore(endDate, beginDate)) {
            throw new ALUtilsException("ALDateUtils#isDateBetween : beginDate must be before endDate");
        }

        return ((JadeDateUtil.isDateBefore(dateToCheck, endDate) || JadeDateUtil.areDatesEquals(dateToCheck, endDate)) && (JadeDateUtil
                .isDateBefore(beginDate, dateToCheck) || JadeDateUtil.areDatesEquals(beginDate, dateToCheck)));
    }

    /**
     * Vérifie si <code>date</code> est le premier jour du mois
     * 
     * @param date
     *            Date à vérifier
     * @return <code>true</code> si <code>date</code> est le premier jour du mois, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception levée si <code>date</code> n'est pas une date valide ou si elle n'a pas pu être parsée
     */
    public static boolean isFirstDay(String date) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALUtilsException("ALDateUtils#isFirstDay :" + date + " is not a valid date");
        }

        Calendar d = ALDateUtils.getCalendarDate(date);

        return !((d != null) && (d.get(Calendar.DAY_OF_MONTH) > 1));
    }

    /**
     * Vérifie si <code>date</code> est le dernier jour du mois
     * 
     * @param date
     *            Date à vérifier
     * @return <code>true</code> si <code>date</code> est le dernier jour du mois, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception levée si <code>date</code> n'est pas une date valide ou si elle n'a pas pu être parsée
     */
    public static boolean isLastDay(String date) throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALUtilsException("ALDateUtils#isFirstDay :" + date + " is not a valid date");
        }

        Calendar d = ALDateUtils.getCalendarDate(date);

        return !((d != null) && (d.get(Calendar.DAY_OF_MONTH) < d.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }

    /**
     * Vérifie si <code>periodToCheck</code> se trouve entre <code>beginPeriod</code> et <code>endPeriod</code>
     * 
     * @param periodToCheck
     *            La période à vérifier
     * @param beginPeriod
     *            La période de début
     * @param endPeriod
     *            La période de fin
     * @return <code>true</code> si <code>periodToCheck</code> se trouve entre <code>beginPeriod</code> et
     *         <code>endPeriod</code>
     * 
     * @throws JadeApplicationException
     *             Exception levée si l'une des date n'est pas valide ou si <code>endDate</code> est avant
     *             <code>beginDate</code>
     */
    public static boolean isPeriodeBetween(String periodToCheck, String beginPeriod, String endPeriod)
            throws JadeApplicationException {

        if (!JadeDateUtil.isGlobazDate(periodToCheck)) {
            throw new ALUtilsException("ALDateUtils#isPeriodeBetween : " + periodToCheck + " is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDate(beginPeriod)) {
            throw new ALUtilsException("ALDateUtils#isPeriodeBetween : " + beginPeriod + " is not a valid period");
        }

        if (!JadeDateUtil.isGlobazDate(endPeriod)) {
            throw new ALUtilsException("ALDateUtils#isPeriodeBetween : " + endPeriod + " is not a valid period");
        }

        if (!JadeDateUtil.isDateBefore(beginPeriod, endPeriod)) {
            throw new ALUtilsException("ALDateUtils#isPeriodeBetween : beginDate must be before endDate");
        }

        return ((JadeDateUtil.isDateMonthYearBefore(periodToCheck, endPeriod) || periodToCheck.equals(endPeriod)) && (JadeDateUtil
                .isDateMonthYearBefore(beginPeriod, periodToCheck) || beginPeriod.equals(periodToCheck)));
    }

    /**
     * Convertit un spy en Gregorian Calendar
     * 
     * @param spy
     * 
     * @return le spy converti
     */
    public static GregorianCalendar spyToGregorianCalendar(BSpy spy) {
        return new GregorianCalendar(Integer.parseInt(spy.getFullData().substring(0, 4)), Integer.parseInt(spy
                .getFullData().substring(4, 6)) - 1, Integer.parseInt(spy.getFullData().substring(6, 8)),
                Integer.parseInt(spy.getFullData().substring(8, 10)), Integer.parseInt(spy.getFullData().substring(10,
                        12)), Integer.parseInt(spy.getFullData().substring(12, 14)));
    }

    public static String XMLGregorianCalendarToGlobazDate(XMLGregorianCalendar date) throws JadeApplicationException {
        if (date == null) {
            throw new ALUtilsException("ALDateUtils#XMLGregorianCalendarToGlobazDate : date is null");
        }

        return JadeDateUtil.getGlobazFormattedDate(date.toGregorianCalendar().getTime());
    }
}
