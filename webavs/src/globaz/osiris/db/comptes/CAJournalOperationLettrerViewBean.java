package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;

/**
 * @author dda
 */
public class CAJournalOperationLettrerViewBean extends CAJournalOperationLettrer implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CALettrageViewBean.
     */
    public CAJournalOperationLettrerViewBean() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CALettrageViewBean.
     * 
     * @param parent
     */
    public CAJournalOperationLettrerViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CALettrageViewBean.
     * 
     * @param session
     */
    public CAJournalOperationLettrerViewBean(BSession session) throws Exception {
        super(session);
    }
}
