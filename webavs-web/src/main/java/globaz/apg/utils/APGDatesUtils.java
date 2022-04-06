package globaz.apg.utils;

import java.util.Calendar;
import java.util.Date;

public class APGDatesUtils {

    public static boolean isMemeAnnee(String date1, String date2) {
        if(date1 == null || date1.length() != 10 || date2 == null || date2.length() != 10) {
            return false;
        }
        return date1.substring(4).equals(date2.substring(4));
    }

    public static boolean isMemeAnnee(int year, Date date2) {
        if(date2 == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);
        return year != calendar.get(Calendar.YEAR);
    }
}
