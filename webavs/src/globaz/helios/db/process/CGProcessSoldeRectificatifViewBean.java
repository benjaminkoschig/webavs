package globaz.helios.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.CGProcessSoldeRectificatif;

public class CGProcessSoldeRectificatifViewBean extends CGProcessSoldeRectificatif implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessSoldeRectificatifViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param parent
     */
    public CGProcessSoldeRectificatifViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param session
     */
    public CGProcessSoldeRectificatifViewBean(BSession session) throws Exception {
        super(session);
    }
}
