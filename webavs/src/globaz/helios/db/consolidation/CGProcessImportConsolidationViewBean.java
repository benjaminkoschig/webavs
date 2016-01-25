package globaz.helios.db.consolidation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.consolidation.CGProcessImportConsolidation;

public class CGProcessImportConsolidationViewBean extends CGProcessImportConsolidation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessImportConsolidationViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessImportConsolidationViewBean.
     * 
     * @param parent
     */
    public CGProcessImportConsolidationViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportConsolidationViewBean.
     * 
     * @param session
     */
    public CGProcessImportConsolidationViewBean(BSession session) throws Exception {
        super(session);
    }
}
