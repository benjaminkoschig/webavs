/**
 * 
 */
package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche pour les dossiers à prendre en compte pour les attestations de versments
 * 
 * @author pta
 * 
 */
public class DossierAttestationVersementComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * nss de l'allocataire
     */
    private String forNssAllocataire = null;
    /**
     * numéro de l'affilié
     */
    private String forNumAffilie = null;

    /**
     * @return the forNssAllocataire
     */
    public String getForNssAllocataire() {
        return forNssAllocataire;
    }

    /**
     * @return the forNumAffilie
     */
    public String getForNumAffilie() {
        return forNumAffilie;
    }

    /**
     * @param forNssAllocataire
     *            the forNssAllocataire to set
     */
    public void setForNssAllocataire(String forNssAllocataire) {
        this.forNssAllocataire = forNssAllocataire;
    }

    /**
     * @param forNumAffilie
     *            the forNumAffilie to set
     */
    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    @Override
    public Class<DossierAttestationVersementComplexModel> whichModelClass() {
        return DossierAttestationVersementComplexModel.class;
    }

}
