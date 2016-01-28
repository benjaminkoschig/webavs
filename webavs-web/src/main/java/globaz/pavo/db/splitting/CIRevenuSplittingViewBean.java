package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Bean d'un revenu de splitting Date de création : (31.10.2002 08:17:50)
 * 
 * @author: Administrator
 */
public class CIRevenuSplittingViewBean extends CIRevenuSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private java.lang.String message = null;
    private java.lang.String msgType = null;

    /**
     * Commentaire relatif au constructeur CIRevenuSplittingViewBean.
     */
    public CIRevenuSplittingViewBean() {
        super();
    }

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
