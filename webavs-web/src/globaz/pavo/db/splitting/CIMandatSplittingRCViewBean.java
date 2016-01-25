package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * ViewBean pour l'en-tête de la gestion des mandats. Date de création : (29.10.2002 09:14:22)
 * 
 * @author: dgi
 */
public class CIMandatSplittingRCViewBean extends CIDossierSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action = null;
    private String message = null;
    private String msgType = null;

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
