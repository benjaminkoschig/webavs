package globaz.aquila.db.journaux;

import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.framework.bean.FWViewBeanInterface;

public class COElementJournalBatchViewBean extends COElementJournalBatch implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pour �cran.
     * 
     * @return Le libell� du journal.
     */
    public String getJournalLibelle() {
        COJournalBatch journal = new COJournalBatch();
        journal.setSession(getSession());

        journal.setIdJournal(getIdJournal());

        try {
            journal.retrieve();

            if (journal.hasErrors() || journal.isNew()) {
                return "";
            }

            return journal.getLibelle();
        } catch (Exception e) {
            return "";
        }
    }
}
