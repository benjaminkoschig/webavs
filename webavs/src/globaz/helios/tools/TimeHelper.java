package globaz.helios.tools;

import globaz.globall.util.JADate;
import globaz.globall.util.JATime;
import java.util.Calendar;

/**
 * Classe : type_conteneur
 * 
 * Description :
 * 
 * Date de création: 3 févr. 04
 * 
 * @author scr
 * 
 */
public abstract class TimeHelper {

    /**
     * Method getCurrentTime.
     * 
     * retourne la date et heure courante au format : dd.mm.yyyy hh:mm:ss
     * 
     * @return String
     */
    public static String getCurrentTime() {

        StringBuffer sb = new StringBuffer();
        Calendar cal = new java.util.GregorianCalendar();
        JADate date = new JADate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        sb.append(date.toStr("."));
        sb.append("  ");
        JATime time = new JATime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        sb.append(time.toStr(":"));
        return sb.toString();
    }

}
