package ch.globaz.al.business.paiement;

import java.util.Collection;

/**
 * Conteneur pour les listes de paiement (liste d�taill�e et par rubrique)
 * 
 * @author jts
 * 
 */
public class PaiementContainer {

    /**
     * Liste d�taill�e
     */
    Collection<PaiementBusinessModel> paiementBusinessList = null;
    /**
     * Liste par rubrique
     */
    Collection<PaiementRecapitulatifBusinessModel> paiementRecapitulatifBusinessList = null;

    /**
     * @return Liste d�taill�e
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
     *            Liste d�taill�e
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
