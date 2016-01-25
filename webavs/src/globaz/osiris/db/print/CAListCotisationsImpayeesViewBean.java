package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;

public class CAListCotisationsImpayeesViewBean extends CACotisationsImpayeesProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListCotisationsImpayeesViewBean() {
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError(getTransaction(), "Le champ email doit être renseigné.");
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
