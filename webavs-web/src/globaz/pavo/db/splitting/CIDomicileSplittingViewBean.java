package globaz.pavo.db.splitting;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (21.10.2002 13:45:53)
 * 
 * @author: Administrator
 */
public class CIDomicileSplittingViewBean extends CIDomicileSplitting implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private java.lang.String message = null;
    private java.lang.String msgType = null;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.10.2002 10:36:03)
     */
    public CIDomicileSplittingViewBean() {

    }

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }
}
