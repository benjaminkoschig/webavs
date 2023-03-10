package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.osiris.process.CAProcessListIrrecouvrable;

public class CAListIrrecouvrableViewBean extends CAProcessListIrrecouvrable implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListIrrecouvrableViewBean() {
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit ?tre renseign?.");
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
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
