package globaz.helios.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.CGProcessImprimerCompteAnnuelConsolide;

public class CGCompteAnnuelConsolideViewBean extends CGProcessImprimerCompteAnnuelConsolide implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGCompteAnnuelConsolideViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGCompteAnnuelConsolideMandatOfasViewBean.
     * 
     * @param parent
     */
    public CGCompteAnnuelConsolideViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGCompteAnnuelConsolideMandatOfasViewBean.
     * 
     * @param session
     */
    public CGCompteAnnuelConsolideViewBean(BSession session) throws Exception {
        super(session);
    }
}
