package globaz.naos.db.tent;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Calendar;

public class AFExportViewBean extends AFExport implements FWViewBeanInterface {

    private static final long serialVersionUID = -1466592247453450968L;

    private static String calendarToAMJ(Calendar c) {
        String day = JadeStringUtil.rightJustifyInteger("" + (c.get(Calendar.DAY_OF_MONTH)), 2);
        String month = JadeStringUtil.rightJustifyInteger("" + (c.get(Calendar.MONTH) + 1), 2);
        String year = "" + c.get(Calendar.YEAR);
        return day + "." + month + "." + year;
    }

    /** y'a un mois */
    public static String getDefaultDateDebut() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        return AFExportViewBean.calendarToAMJ(c);
    }

    /** aujourd'hui */
    public static String getDefaultDateFin() {
        Calendar c = Calendar.getInstance();
        return AFExportViewBean.calendarToAMJ(c);
    }

    public AFExportViewBean() {
        super();
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateDebut(String _dateDebut) {
        dateDebut = _dateDebut;
    }

    public void setDateFin(String _dateFin) {
        dateFin = _dateFin;
    }
}
