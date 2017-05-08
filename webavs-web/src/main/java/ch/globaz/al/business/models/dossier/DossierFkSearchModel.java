/**
 * 
 */
package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche simple de dossier. Utilisé en particulier pour vérifier l'existance d'un dossier
 * 
 * @author jts
 * 
 */
public class DossierFkSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche par l'état du dossier
     */
    private String forEtatDossier = null;
    /**
     * Recherche par l'identifiant de l'allocataire
     */
    private String forIdAllocataire = null;
    /**
     * Recherche par l'idnetifiant du dossier
     */
    private String forIdDossier = null;

    /**
     * Recherche par l'id tiers beneficiaire
     */
    private String forIdTiersBeneficiaire = null;

    /**
     * recherche par le statut
     */

    private String forStatut = null;

    /**
     * 
     * @return forEtatDossier
     */
    public String getForEtatDossier() {
        return forEtatDossier;
    }

    /**
     * @return the forIdAllocataire
     */
    public String getForIdAllocataire() {
        return forIdAllocataire;
    }

    /**
     * @return the ForIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forStatut
     */
    public String getForStatut() {
        return forStatut;
    }

    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    /**
     * 
     * @param forEtatDossier
     *            : the forEtatDossier to set
     */
    public void setForEtatDossier(String forEtatDossier) {
        this.forEtatDossier = forEtatDossier;
    }

    /**
     * @param forIdAllocataire
     *            the forIdAllocataire to set
     */
    public void setForIdAllocataire(String forIdAllocataire) {
        this.forIdAllocataire = forIdAllocataire;
    }

    /**
     * @param forIdDossier
     *            the ForIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forStatut
     *            the forStatut to set
     */
    public void setForStatut(String forStatut) {
        this.forStatut = forStatut;
    }

    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return DossierFkModel.class;
    }

}
