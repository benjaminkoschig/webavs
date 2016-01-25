package globaz.aquila.db.poursuite;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * Représente le model de la vue "_de"
 * 
 * @author Arnaud Dostes, 05-oct-2004
 */
public class COContentieuxViewBean extends COContentieux implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean idExtourne = true;

    /**
     * @return the idExtourne
     */
    public boolean isIdExtourne() {
        return idExtourne;
    }

    /**
     * @param idExtourne
     *            the idExtourne to set
     */
    public void setIdExtourne(boolean idExtourne) {
        this.idExtourne = idExtourne;
    }
}
