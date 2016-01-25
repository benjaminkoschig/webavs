package globaz.osiris.utils;

import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAHolidays;
import java.io.File;
import java.net.URL;
import java.util.Calendar;

public class CADateUtil {

    private static JACalendarGregorian calendarWithHolidays;
    private static final String FILE_HOLIDAYS_XML = "/holidays.xml";

    /**
     * Test si jour tombe sur un week end ou un jour férié. Si oui incrément de 1 le jour.
     * 
     * @param myDate
     * @return
     */
    public static JADate getDateOuvrable(JADate myDate) throws Exception {
        if (CADateUtil.calendarWithHolidays == null) {
            URL url = new CADateUtil().getClass().getResource(CADateUtil.FILE_HOLIDAYS_XML);

            if (url != null) {
                File f = new File(url.getFile());
                CADateUtil.calendarWithHolidays = new JACalendarGregorian(new JAHolidays(f.getPath()));
            } else {
                CADateUtil.calendarWithHolidays = new JACalendarGregorian();
            }
        }

        return CADateUtil.calendarWithHolidays.getNextWorkingDay(myDate);
    }

    /**
     * @param myDate
     * @return le prochain jour ouvrable sans changer de mois.
     * @throws Exception
     */
    public static JADate getDateOuvrableMoisCourant(JADate myDate) throws Exception {
        if (CADateUtil.calendarWithHolidays == null) {
            URL url = new CADateUtil().getClass().getResource(CADateUtil.FILE_HOLIDAYS_XML);

            if (url != null) {
                File f = new File(url.getFile());
                CADateUtil.calendarWithHolidays = new JACalendarGregorian(new JAHolidays(f.getPath()));
            } else {
                CADateUtil.calendarWithHolidays = new JACalendarGregorian();
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, myDate.getMonth() - 1);

        if (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - myDate.getDay() <= 5) {
            return CADateUtil.calendarWithHolidays.getPreviousWorkingDay(myDate);
        } else {
            return CADateUtil.calendarWithHolidays.getNextWorkingDay(myDate);
        }
    }
}
