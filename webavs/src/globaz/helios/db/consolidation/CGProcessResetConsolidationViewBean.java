package globaz.helios.db.consolidation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.consolidation.CGProcessResetConsolidation;

public class CGProcessResetConsolidationViewBean extends CGProcessResetConsolidation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessResetConsolidationViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessImportConsolidationViewBean.
     * 
     * @param parent
     */
    public CGProcessResetConsolidationViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportConsolidationViewBean.
     * 
     * @param session
     */
    public CGProcessResetConsolidationViewBean(BSession session) throws Exception {
        super(session);
    }
}
