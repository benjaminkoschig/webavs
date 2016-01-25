package globaz.helios.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.helios.process.exportecritures.CGProcessExportEcritures;

/**
 * @author SCO 30 mars 2010
 */
public class CGProcessExportEcrituresViewBean extends CGProcessExportEcritures implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CGProcessExportEcrituresViewBean.
     * 
     * @throws Exception
     */
    public CGProcessExportEcrituresViewBean() throws Exception {
        super();
    }

    /**
     * Constructor for CGProcessExportEcrituresViewBean.
     * 
     * @param parent
     */
    public CGProcessExportEcrituresViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessExportEcrituresViewBean.
     * 
     * @param session
     */
    public CGProcessExportEcrituresViewBean(BSession session) throws Exception {
        super(session);
    }
}
