/*
 * Créé le 8 sept. 05
 */
package globaz.ij.vb.lots;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJLotViewBean extends IJLot implements FWViewBeanInterface {

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
        return getSession().getCodeLibelle(getCsEtat());
    }

    /**
     * Pour savoir si le lot contient au moins une prestation
     * 
     * @return true si le lot contien au moins une prestation
     */
    public boolean hasPrestations() {
        int nbPrestations = 0;

        if (!JadeStringUtil.isIntegerEmpty(getIdLot())) {

            IJPrestationManager manager = new IJPrestationManager();
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
        return getCsEtat().equals(IIJLot.CS_COMPENSE);
    }

    /**
     * pour savoir si le lot est validé
     * 
     * @return true si validé, false sinon.
     */
    public boolean isValide() {
        return getCsEtat().equals(IIJLot.CS_VALIDE);
    }
}
