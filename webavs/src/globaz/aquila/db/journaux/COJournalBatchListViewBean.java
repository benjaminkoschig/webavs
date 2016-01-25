package globaz.aquila.db.journaux;

import globaz.aquila.db.access.journal.COJournalBatchManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class COJournalBatchListViewBean extends COJournalBatchManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COJournalBatchViewBean();
    }
}
