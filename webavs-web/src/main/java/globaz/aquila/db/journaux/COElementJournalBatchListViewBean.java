package globaz.aquila.db.journaux;

import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class COElementJournalBatchListViewBean extends COElementJournalBatchManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COElementJournalBatchViewBean();
    }
}
