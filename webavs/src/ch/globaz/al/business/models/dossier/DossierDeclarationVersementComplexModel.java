package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Classe du modèle pour l'impression des déclaration de versement
 * 
 * @author PTA
 * 
 */

public class DossierDeclarationVersementComplexModel extends
/* DossierComplexModel */JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * activité de l'allocataire
     */
    String activiteAllocataire = null;
    /**
     * type de bonification
     */
    String bonification = null;
    /**
     * identifiant du dossier
     */
    String idDossier = null;
    /**
     * identifiant du tiers Allocataire
     */

    String idTiersAllocataire = null;

    /**
     * tiers bénéficiaire du dossier
     */

    String idTiersBeneficiaire = null;

    /**
     * nom allocataire
     */

    String nomAllocataire = null;

    /**
     * numéro de l'affilié
     */
    String numeroAffilie = null;
    /**
     * permis de l'allocataire
     */
    String permisAllocataire = null;
    /**
     * prénom allocataire
     */
    String prenomAllocataire = null;

    /**
     * impot à la source
     */

    Boolean retenueImpot = null;

    /**
     * @return the activiteAllocataire
     */
    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    /**
     * @return the bonification
     */
    public String getBonification() {
        return bonification;
    }

    @Override
    public String getId() {

        return null;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    public String getIdTiersAllocataire() {
        return idTiersAllocataire;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return the permisAllocataire
     */
    public String getPermisAllocataire() {
        return permisAllocataire;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    /**
     * @return the retenueImpot
     */
    public Boolean getRetenueImpot() {
        return retenueImpot;
    }

    @Override
    public String getSpy() {
        return null;
    }

    /**
     * @param activiteAllocataire
     *            the activiteAllocataire to set
     */
    public void setActiviteAllocataire(String activiteAllocataire) {
        this.activiteAllocataire = activiteAllocataire;
    }

    /**
     * @param bonification
     *            the bonification to set
     */
    public void setBonification(String bonification) {
        this.bonification = bonification;
    }

    @Override
    public void setId(String id) {
        // Do nothing

    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdTiersAllocataire(String idTiersAllocataire) {
        this.idTiersAllocataire = idTiersAllocataire;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * @param permisAllocataire
     *            the permisAllocataire to set
     */
    public void setPermisAllocataire(String permisAllocataire) {
        this.permisAllocataire = permisAllocataire;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    /**
     * @param retenueImpot
     *            the retenueImpot to set
     */
    public void setRetenueImpot(Boolean retenueImpot) {
        this.retenueImpot = retenueImpot;
    }

    @Override
    public void setSpy(String spy) {
        // Do nothing

    }

}
