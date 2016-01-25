package ch.globaz.utils.periode;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.globaz.common.domaine.GroupePeriodes;
import ch.globaz.common.domaine.Periode;

public class GroupePeriodesResolver {

    public interface Each<T> {
        public String date(T t);
    }

    public interface EachPeriode<T> {
        public String[] dateDebutFin(T t);
    }

    public static <T> List<String> genearateListDateDebutFin(Collection<T> list, Each<T> each) {
        List<String> listReturn = new ArrayList<String>();
        for (T t : list) {
            listReturn.add(each.date(t));
        }
        return listReturn;
    }

    public static <T> GroupePeriodes genearateListPeriode(Collection<T> list, EachPeriode<T> each) {
        GroupePeriodes listReturn = new GroupePeriodes();
        for (T t : list) {
            String dates[] = each.dateDebutFin(t);
            listReturn.add(new Periode(dates[0], dates[1]));
        }
        return listReturn;
    }

    /**
     * Permet de définir si deux date sont dans le même mois. Les deux dates passées en paramétrer peuvent être de
     * différent format. Voir la fonction resolveYear qui détermine les différents format pris en charge.
     * 
     * @param date1
     * @param date2
     * @return boolean
     */
    public static boolean isDateInSmaeYear(String date1, String date2) {
        String year1 = GroupePeriodesResolver.resolveYear(date1);
        String year2 = GroupePeriodesResolver.resolveYear(date2);
        return year1.equals(year2);
    }

    /**
     * Retourne la valeur date la plus grand s'il existe une date qui est null(vide ou 0) celle-ci n'est pas prit en
     * compte
     * 
     * @param dates
     * @return
     */
    public static String resolveDateMax(List<String> dates) {
        String dateMax = null;
        if (dates == null) {
            throw new IllegalArgumentException("Unable to resolveDateMax, the dates is null!");
        }
        for (String date : dates) {

            if ((dateMax == null) || JadeDateUtil.isDateMonthYearBefore(dateMax, date)) {
                dateMax = date;
            }
        }
        return dateMax;
    }

    /**
     * Permet de détermine la date la plus petit, si mustReturnNullValue et définit a vrais on retourne null si il
     * existe une date null(0 ou vide)
     * 
     * @param dates
     * @param mustReturNullValue
     * @return la date la plus grande
     */
    public static String resolveDateMin(Collection<String> dates, boolean mustReturNullValue) {
        String dateMin = null;

        for (String date : dates) {
            if (JadeStringUtil.isBlankOrZero(date) && mustReturNullValue) {
                return null;
            }
            if ((dateMin == null) || JadeDateUtil.isDateMonthYearAfter(dateMin, date)) {
                dateMin = date;
            }
        }
        return dateMin;
    }

    /**
     * Retourne la valeur date la plus petit s'il existe une date qui est null(vide ou 0) celle-ci n'est pas prit en
     * compte
     * 
     * @param dates
     * @return
     */
    public static String resolveDateMin(List<String> dates) {
        return GroupePeriodesResolver.resolveDateMin(dates, false);
    }

    private static String resolveYear(String date) {
        String year = null;

        if (JadeDateUtil.isGlobazDateMonthYear(date)) {
            year = date.substring(3);
        } else if (JadeDateUtil.isGlobazDate(date)) {
            year = date.substring(6);
        } else {
            throw new IllegalArgumentException("Unable  to resolve the year of this date: +" + date
                    + ", is not a valide format");
        }
        return year;
    }

}
