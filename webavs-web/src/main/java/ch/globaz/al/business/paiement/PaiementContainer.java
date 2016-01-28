package ch.globaz.al.business.paiement;

import java.util.Collection;

/**
 * Conteneur pour les listes de paiement (liste détaillée et par rubrique)
 * 
 * @author jts
 * 
 */
public class PaiementContainer {

    /**
     * Liste détaillée
     */
    Collection<PaiementBusinessModel> paiementBusinessList = null;
    /**
     * Liste par rubrique
     */
    Collection<PaiementRecapitulatifBusinessModel> paiementRecapitulatifBusinessList = null;

    /**
     * @return Liste détaillée
     */
    public Collection<PaiementBusinessModel> getPaiementBusinessList() {
        return paiementBusinessList;
    }

    /**
     * @return Liste par rubrique
     */
    public Collection<PaiementRecapitulatifBusinessModel> getPaiementRecapitulatifBusinessList() {
        return paiementRecapitulatifBusinessList;
    }

    /**
     * @param paiementBusinessList
     *            Liste détaillée
     */
    public void setPaiementBusinessList(Collection<PaiementBusinessModel> paiementBusinessList) {
        this.paiementBusinessList = paiementBusinessList;
    }

    /**
     * @param paiementRecapitulatifBusinessList
     *            Liste par rubrique
     */
    public void setPaiementRecapitulatifBusinessList(
            Collection<PaiementRecapitulatifBusinessModel> paiementRecapitulatifBusinessList) {
        this.paiementRecapitulatifBusinessList = paiementRecapitulatifBusinessList;
    }
}
