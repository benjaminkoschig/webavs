/**
 * 
 */
package globaz.perseus.vb.lot;

import globaz.globall.db.BSession;
import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * @author BSC
 */
public abstract class PFAbstractPrestationViewBean extends BJadePersistentObjectViewBean {

    /**
     * Constructeur simple
     */
    public PFAbstractPrestationViewBean() {
    }

    public abstract String getDetailAssure();

    public abstract String getEtat();

    public abstract String getIdDecision();

    public abstract String getIdFacture();

    public abstract String getIdTiersRequerant();

    public abstract String getMontant();

    public abstract String getPeriode();

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    public abstract String getTypeLot();

    public abstract boolean isPCFamilles();

}
