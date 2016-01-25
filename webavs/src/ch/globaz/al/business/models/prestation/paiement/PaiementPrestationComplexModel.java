package ch.globaz.al.business.models.prestation.paiement;

/**
 * Modèle de base pour le paiement direct de prestations AF. Ce modèle est le parent de tous les modèles liés aux
 * paiement direct de prestations
 * 
 * @author jts
 * 
 */
public class PaiementPrestationComplexModel extends CompensationPaiementPrestationComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Type de bonification de la récap
     */
    private String bonificationRecap = null;
    /**
     * Id du dossier
     */
    private String idDossier = null;
    /**
     * Id du tiers allocataire
     */
    private String idTiersAllocataire = null;
    /**
     * Id du tiers bénéficiaire du détail de la prestation
     */
    private String idTiersBeneficiaire = null;

    /**
     * Nom de l'allocataire lié au dossier
     */
    private String nomAllocataire = null;

    /**
     * NSS de l'allocataire
     */
    private String nssAllocataire = null;

    /**
     * Numéro de rubrique comptable
     */
    private String numeroCompte = null;

    /**
     * Numéro de facture
     */
    private String numeroFacture = null;

    /**
     * Prénom de l'allocataire lié au dossier
     */
    private String prenomAllocataire = null;

    /**
     * @return the bonificationRecap
     */
    public String getBonificationRecap() {
        return bonificationRecap;
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

    /**
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the nomAllocataire
     */
    public String getNomAllocataire() {
        return nomAllocataire;
    }

    /**
     * @return the nssAllocataire
     */
    public String getNssAllocataire() {
        return nssAllocataire;
    }

    /**
     * @return the numeroCompte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * @return the numeroFacture
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * @return the prenomAllocataire
     */
    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    /**
     * @param bonificationRecap
     *            the bonificationRecap to set
     */
    public void setBonificationRecap(String bonificationRecap) {
        this.bonificationRecap = bonificationRecap;
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

    /**
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * @param nomAllocataire
     *            the nomAllocataire to set
     */
    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    /**
     * @param nssAllocataire
     *            the nssAllocataire to set
     */
    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    /**
     * @param numeroCompte
     *            the numeroCompte to set
     */
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    /**
     * @param numeroFacture
     *            the numeroFacture to set
     */
    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    /**
     * @param prenomAllocataire
     *            the prenomAllocataire to set
     */
    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }
}
