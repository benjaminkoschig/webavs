/*
 * Créé le 7 avr. 05
 */
package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.process.journal.CAProcessImpressionsJournal;
import java.util.ArrayList;

/**
 * @author jts 7 avr. 05 10:45:24
 */
public class CAListImpressionsJournalViewBean extends CAProcessImpressionsJournal implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListImpressionsJournalViewBean() {
        super();
    }

    public boolean validerPourExecution() {
        CAOperationManager manager = new CAOperationManager();
        manager.setSession(getSession());
        ArrayList<String> idTypeOperationLike = new ArrayList<String>();
        idTypeOperationLike.add(APIOperation.CAECRITURE);
        manager.setVueOperationCpteAnnexe("true");
        manager.setVueOperationCaCcSe(new Boolean(true));
        manager.setCompteAnnexeBetween(Boolean.TRUE);
        manager.setFromIdExterneRole(getFromIdExterneRole());
        manager.setToIdExterneRole(getToIdExterneRole());
        manager.setForIdTypeOperationLikeIn(idTypeOperationLike);

        manager.setForIdJournal(getIdJournal());
        try {
            if (manager.getCount() > CAApplication.getApplicationOsiris().getCAParametres()
                    .getLimiteImpressionEcritureJournal()) {
                this._addError(getSession().getLabel("JOURNAL_CONTIENT_TROP_DECRITURES"));
                if (getSession().hasErrors()) {
                    setMessage(getSession().getErrors().toString());
                    setMsgType(FWViewBeanInterface.ERROR);
                }

                return FWViewBeanInterface.OK.equals(getMsgType());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
