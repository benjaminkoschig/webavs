package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.process.CAIProcessListComptesALettrer;

public class CAListComptesALettrerViewBean extends CAIProcessListComptesALettrer implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListComptesALettrerViewBean() {
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
    }
}
