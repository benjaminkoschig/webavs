package globaz.helios.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.importecritures.CGProcessImportEcritures;

public class CGProcessImportEcrituresViewBean extends CGProcessImportEcritures implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws Exception
     */
    public CGProcessImportEcrituresViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param parent
     */
    public CGProcessImportEcrituresViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImportEcritures.
     * 
     * @param session
     */
    public CGProcessImportEcrituresViewBean(BSession session) throws Exception {
        super(session);
    }
}
