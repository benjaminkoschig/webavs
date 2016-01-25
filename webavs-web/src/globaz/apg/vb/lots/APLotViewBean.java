/*
 * Créé le 6 juin 05
 */
package globaz.apg.vb.lots;

import globaz.apg.api.lots.IAPLot;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APLotViewBean extends APLot implements FWViewBeanInterface {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * getter pour l'attribut etat lot libelle
     * 
     * @return la valeur courante de l'attribut etat droit libelle
     */
    public String getEtatLotLibelle() {
        return getSession().getCodeLibelle(getEtat());
    }

    /**
     * Pour savoir si le lot contient au moins une prestation
     * 
     * @return true si le lot contien au moins une prestation
     */
    public boolean hasPrestations() {
        int nbPrestations = 0;

        if (!JadeStringUtil.isIntegerEmpty(getIdLot())) {

            APPrestationManager manager = new APPrestationManager();
            manager.setSession(getSession());
            manager.setForIdLot(getIdLot());
            try {
                nbPrestations = manager.getCount();
            } catch (Exception e) {
                nbPrestations = 0;
            }
        }

        return nbPrestations > 0;
    }

    /**
     * pour savoir si le lot est compensé
     * 
     * @return true si compensé, false sinon.
     */
    public boolean isCompense() {
        return getEtat().equals(IAPLot.CS_COMPENSE);
    }

    /**
     * pour savoir si le lot est validé
     * 
     * @return true si validé, false sinon.
     */
    public boolean isValide() {
        return getEtat().equals(IAPLot.CS_VALIDE);
    }
}
