package globaz.helios.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.CGProcessImprimerCompteAnnuel;

public class CGCompteAnnuelViewBean extends CGProcessImprimerCompteAnnuel implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGCompteAnnuelViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGCompteAnnuelConsolideViewBean.
     * 
     * @param parent
     */
    public CGCompteAnnuelViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGCompteAnnuelConsolideViewBean.
     * 
     * @param session
     */
    public CGCompteAnnuelViewBean(BSession session) throws Exception {
        super(session);
    }
}
