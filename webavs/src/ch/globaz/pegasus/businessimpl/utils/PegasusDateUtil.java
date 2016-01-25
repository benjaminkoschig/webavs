package ch.globaz.pegasus.businessimpl.utils;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import ch.globaz.common.business.language.LanguageResolver;

public class PegasusDateUtil {

    public static String addMonths(String globazDate, int nb) {

        String result = null;
        if (JadeDateUtil.isGlobazDateMonthYear(globazDate)) {
            result = JadeDateUtil.addMonths("01." + globazDate, nb).substring(3);
        } else if (JadeDateUtil.isGlobazDate(globazDate)) {
            result = JadeDateUtil.addMonths(globazDate, nb);
        }
        return result;
    }

    /**
     * Convertit une date MM.YYYY à 01.MM.YYYY
     * 
     * @param string mm.yyyy ou mm.yy ou jj.mm.yy ou jj.mm.yyyy
     * 
     * @return
     * @throws Exception
     */

    public static String convertToJadeDate(String date) {
        if (JadeDateUtil.isGlobazDate(date)) {
            return date;
        }
        if (JadeDateUtil.isGlobazDateMonthYear(date)) {
            return "01." + date;
        }
        return null;
    }

    /**
     * retourne le nombre de jours dans une année
     * 
     * @param date
     * @return
     */
    public final static Integer getDayInYear(String date) {
        Calendar ca1 = JadeDateUtil.getGlobazCalendar(date);
        GregorianCalendar gca = new GregorianCalendar();
        int year = ca1.get(Calendar.YEAR);
        if (gca.isLeapYear(year)) {
            return 366;
        } else {
            return 365;
        }
    }

    /**
     * Donne le nombre de jour jusqu'a la date en parametre
     * 
     * @param date
     * @return
     */
    public final static Integer getDayOfYear(String date) {
        Calendar ca1 = JadeDateUtil.getGlobazCalendar(date);
        int DAY_OF_YEAR = ca1.get(Calendar.DAY_OF_YEAR);
        return DAY_OF_YEAR;
    }

    /**
     * 
     * @param monthNumber , conforme à java.util.Date, autrement dit de 0 à 11!!!!
     * @param year , l'année sur 2 ou 4 chiffres
     * @return
     */
    public static int getLastDayOfMonth(int monthNumber, int year) {

        if (!((monthNumber == 0) && (year == 0))) {
            Calendar month = Calendar.getInstance();
            month.set(Calendar.MONTH, monthNumber);
            month.set(Calendar.YEAR, year);
            return month.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }

    /**
     * Retourne le nombre de jours dans un mois.
     * 
     * @param string mm.yyyy ou mm.yy ou jj.mm.yy ou jj.mm.yyyy
     * 
     * @return
     * @throws Exception
     */

    public static Integer getLastDayOfMonth(String date) {
        String dateStr = date;
        Date dateObj = null;

        if (JadeDateUtil.isGlobazDateMonthYear(date)) {
            dateStr = "01." + date;
        }

        if (JadeDateUtil.isGlobazDate(dateStr)) {
            dateObj = JadeDateUtil.getGlobazDate(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObj);
            return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } else {
            throw new RuntimeException("The param date is note a valide globaz date");
        }
    }

    /**
     * Retourne une date formatée avec le mois en littéral, selon la langue choisie pour la session.<br>
     * Ce formatteur est prévu initialement pour des entêtes de document qui spécifient la date de la rédaction.<br>
     * <br>
     * <b>Exemple:</b> Pour un utilisateur français, la date 05.07.2010 retournera "5 juillet 2010".
     * 
     * @param jadeDate date globaz (dd.mm.yyyy) à formater.
     * @return la date formatée en "jour mois_litteral annee".
     */
    public final static String getLitteralDate(String jadeDate) {
        final Calendar cal = JadeDateUtil.getGlobazCalendar(jadeDate);

        final String strMonth = JACalendar.getMonthName(cal.get(Calendar.MONTH) + 1, BSessionUtil
                .getSessionFromThreadContext().getIdLangue());

        return cal.get(Calendar.DAY_OF_MONTH) + " " + strMonth + " " + cal.get(Calendar.YEAR);
    }

    /**
     * Cette méthode a été réalisée pour permettre de retourner des dates traduites en fonction de l'id de langue passée
     * en paramètre.
     * 
     * @param jadeDate
     * @param idLangue
     * @return une date formatée -> 1er mars 2015, 1. März 2015
     */
    public final static String getLitteralDateByTiersLanguage(String jadeDate, String idLangue) {

        idLangue = LanguageResolver.resolveISOCodeToString(idLangue);
        return JACalendar.format(jadeDate, idLangue);
    }

    /**
     * Methode qui retourne un objet java.util.Date en récupérant en paramètre un format date de type "mm.yyyy" ou
     * "mm.yy". Le jour est automatiquement défini en fonction du booleen isMonthBegin, si true, le jour est setter a la
     * valeur 1 (debut du mois), sinon, le dernier jour du mois
     * 
     * @param pegasusMonthYearDate
     * @param isMonthBegin
     * @return
     */
    public static Date getPegasusMonthYearDate(String pegasusMonthYearDate, boolean isMonthBegin) {
        if (!JadeStringUtil.isEmpty(pegasusMonthYearDate)
                && ((pegasusMonthYearDate.length() == 5) || (pegasusMonthYearDate.length() == 7))) {
            try {
                // Si debut du mois
                if (isMonthBegin) {
                    pegasusMonthYearDate = "01." + pegasusMonthYearDate;
                } else {
                    int year = Integer.parseInt(pegasusMonthYearDate.substring(3));
                    int month = Integer.parseInt(pegasusMonthYearDate.substring(0, 2));
                    pegasusMonthYearDate = PegasusDateUtil.getLastDayOfMonth(month - 1, year) + "."
                            + pegasusMonthYearDate;
                }

                SimpleDateFormat formatter = new SimpleDateFormat(pegasusMonthYearDate.length() == 10 ? "dd.MM.yyyy"
                        : "dd.MM.yy");
                formatter.setLenient(false);
                return formatter.parse(pegasusMonthYearDate);
            } catch (ParseException e) {
                JadeCodingUtil.catchException(PegasusDateUtil.class, "getPegasusMonthYearDate", e);
            }
        }
        return null;
    }

    /**
     * Retourne la date du jour au fomat mois-annéée, mm.aaaa
     * 
     * @return String pegasusdate, la date du jour format mm.aaaa
     */
    public static String getPegasusMonthYearDateNow() {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int mois = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        String pegasusDate = "";
        if (mois < 10) {
            pegasusDate += "0" + mois;
        } else {
            pegasusDate += mois;
        }
        pegasusDate += "." + year;

        return pegasusDate;

    }

    /**
     * Convertit la date au format jj.mm.aaaa, avec comme jour le dernier jour du mois
     * 
     * @param oldDate Date à convertir (format mm.aaaa ou jj.mm.aaaa)
     * @return Date convertie, ou null si la date était null
     */
    public final static String setDateMaxDayOfMonth(String oldDate) {
        if (JadeStringUtil.isEmpty(oldDate)) {
            return oldDate;
        }

        if (JadeDateUtil.isGlobazDateMonthYear(oldDate)) {
            oldDate = "01." + oldDate;
        }
        Calendar cal = JadeDateUtil.getGlobazCalendar(oldDate);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return JadeDateUtil.getGlobazFormattedDate(cal.getTime());
    }
}
