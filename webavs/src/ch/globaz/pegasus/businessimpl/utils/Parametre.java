package ch.globaz.pegasus.businessimpl.utils;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;

public class Parametre {

    /**
     * Soustrait 1 mois a la date passé en paramétre
     * 
     * @param String
     *            date
     * @return String(FORMAT_MMsYYYY)
     */
    public static String removeOneMonth(String str) throws JAException {
        JACalendarGregorian calendarGregorian = new JACalendarGregorian();
        String date = null;
        date = JACalendar.format(calendarGregorian.addMonths(str, -1), JACalendar.FORMAT_MMsYYYY);
        return date;
    }

}
