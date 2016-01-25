package globaz.helios.db.consolidation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.consolidation.print.CGProcessImprimerConsolidationParMois;

public class CGProcessImprimerConsolidationParMoisViewBean extends CGProcessImprimerConsolidationParMois implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessImprimerConsolidationParMoisViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessImprimerConsolidationParMoisViewBean.
     * 
     * @param parent
     */
    public CGProcessImprimerConsolidationParMoisViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImprimerConsolidationParMoisViewBean.
     * 
     * @param session
     */
    public CGProcessImprimerConsolidationParMoisViewBean(BSession session) throws Exception {
        super(session);
    }
}
