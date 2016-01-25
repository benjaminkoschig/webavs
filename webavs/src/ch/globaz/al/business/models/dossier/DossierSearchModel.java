/**
 * 
 */
package ch.globaz.al.business.models.dossier;

/**
 * Modèle de recherche simple de dossier. Utilisé en particulier pour vérifier l'existance d'un dossier
 * 
 * @author jts
 * 
 */
public class DossierSearchModel extends DossierFkSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * recherche sur l'activié de l'allocataire
     */
    private String forActiviteAllocataire = null;

    /**
     * Recherche sur la date de fin de validité du dossier
     */
    private String forFinValidite = null;
    /**
     * Recherche sur le tiers bénéficiaire
     */

    private String forIdTiersBeneficiaire = null;
    /**
     * Recherche par numéro d'affilié
     */
    private String forNumeroAffilie = null;

    /**
     * @return the forActiviteAllocataire
     */
    public String getForActiviteAllocataire() {
        return forActiviteAllocataire;
    }

    /**
     * @return the forFinValidite
     */
    public String getForFinValidite() {
        return forFinValidite;
    }

    /**
     * @return the forIdTiersBeneficiaire
     */
    public String getForIdTiersBeneficiaire() {
        return forIdTiersBeneficiaire;
    }

    /**
     * @return the forNumeroAffilie
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * @param forActiviteAllocataire
     *            the forActiviteAllocataire to set
     */
    public void setForActiviteAllocataire(String forActiviteAllocataire) {
        this.forActiviteAllocataire = forActiviteAllocataire;
    }

    /**
     * @param forFinValidite
     *            the forFinValidite to set
     */
    public void setForFinValidite(String forFinValidite) {
        this.forFinValidite = forFinValidite;
    }

    /**
     * @param forIdTiersBeneficiaire
     *            the forIdTiersBeneficiaire to set
     */
    public void setForIdTiersBeneficiaire(String forIdTiersBeneficiaire) {
        this.forIdTiersBeneficiaire = forIdTiersBeneficiaire;
    }

    /**
     * @param forNumeroAffilie
     *            the forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DossierModel> whichModelClass() {
        return DossierModel.class;
    }

}
