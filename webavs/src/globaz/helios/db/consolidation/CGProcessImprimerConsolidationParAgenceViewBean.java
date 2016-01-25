package globaz.helios.db.consolidation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.consolidation.print.CGProcessImprimerConsolidationParAgence;

public class CGProcessImprimerConsolidationParAgenceViewBean extends CGProcessImprimerConsolidationParAgence implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessImprimerConsolidationParAgenceViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessImprimerConsolidationParMoisViewBean.
     * 
     * @param parent
     */
    public CGProcessImprimerConsolidationParAgenceViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImprimerConsolidationParMoisViewBean.
     * 
     * @param session
     */
    public CGProcessImprimerConsolidationParAgenceViewBean(BSession session) throws Exception {
        super(session);
    }
}
