package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.osiris.process.CAProcessListDossierEtape;

public class CAListDossiersEtapeViewBean extends CAProcessListDossierEtape implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListDossiersEtapeViewBean() {
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
