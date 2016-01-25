package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.osiris.process.CAProcessListRecapParRubriquesExcel;

public class CAListRecapParRubriquesExcelViewBean extends CAProcessListRecapParRubriquesExcel implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListRecapParRubriquesExcelViewBean() {
    }

    /**
     * Return la date actuelle.
     * 
     * @return date du jour au fomat JJ.MM.AAAA
     */
    public String getFormatedDateToday() {
        return JACalendar.todayJJsMMsAAAA();
    }

}
