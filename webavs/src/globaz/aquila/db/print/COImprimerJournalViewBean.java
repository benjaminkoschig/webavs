package globaz.aquila.db.print;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.print.journal.COImprimerJournal;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;

public class COImprimerJournalViewBean extends COImprimerJournal implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public COImprimerJournalViewBean() throws Exception {
        super(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA));
    }

    /**
     * @param session
     */
    public COImprimerJournalViewBean(BSession session) {
        super(session);
    }

}
