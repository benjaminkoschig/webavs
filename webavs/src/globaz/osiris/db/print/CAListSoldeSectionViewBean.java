package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.process.CAIProcessListSoldeSection;

public class CAListSoldeSectionViewBean extends CAIProcessListSoldeSection implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListSoldeSectionViewBean() {
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
