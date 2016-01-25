package globaz.helios.db.consolidation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.consolidation.CGProcessExportConsolidation;

public class CGProcessExportConsolidationViewBean extends CGProcessExportConsolidation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessExportConsolidationViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessExportConsolidationViewBean.
     * 
     * @param parent
     */
    public CGProcessExportConsolidationViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessExportConsolidationViewBean.
     * 
     * @param session
     */
    public CGProcessExportConsolidationViewBean(BSession session) throws Exception {
        super(session);
    }
}
