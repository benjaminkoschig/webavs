package ch.globaz.common.util;

import ch.globaz.common.domaine.Date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static String getFirstDayOfMonth(LocalDate date) {
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(date.getYear(), date.getMonthValue() - 1, 1);

        SimpleDateFormat format = new SimpleDateFormat(Date.DATE_PATTERN_SWISS);
        return format.format(calendar.getTime());
    }

    public static String getLastDayOfMonth(LocalDate date) {
        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(date.getYear(), date.getMonthValue() - 1, date.lengthOfMonth());

        SimpleDateFormat format = new SimpleDateFormat(Date.DATE_PATTERN_SWISS);
        return format.format(calendar.getTime());
    }
}
