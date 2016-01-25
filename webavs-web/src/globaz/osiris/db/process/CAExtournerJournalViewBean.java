package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.process.journal.CAProcessExtournerJournal;

public class CAExtournerJournalViewBean extends CAProcessExtournerJournal implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAExtournerJournalViewBean() {
        super();
    }

    /**
     * Retourne le journal
     * 
     * @return
     */
    public String getLibelleJournal() {
        if (!JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            try {
                CAJournal journal = new CAJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());

                journal.retrieve(getTransaction());

                if (!journal.isNew()) {
                    return journal.getLibelle();
                }

                return "";
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.WARNING, this.getClass().getName());
            }
        }
        return "";
    }
}
