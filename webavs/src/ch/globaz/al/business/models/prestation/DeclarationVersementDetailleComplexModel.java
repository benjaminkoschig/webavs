package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe de détail de prestation. Il est utilisé pour l'impression des déclarations de versement détaillées
 * 
 * @author PTA
 * 
 */
public class DeclarationVersementDetailleComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Indique si l'edition de prestation prend en compte allocataire ou tiers bénéficiaire
     */
    private Boolean attestationAlloc = null;
    /**
     * date de naissance de l'enfant
     */
    public String dateNaissanceEnfant = null;
    /**
     * date de versement de la prestation
     */
    public String dateVersement = null;
    /**
     * id du dossier lié à la prestation
     */

    private String idDossier = null;
    /**
     * montant du détail prestation
     */
    public String montantDetailPrestation = null;
    /**
     * Nom de l'enfant
     */
    public String nomEnfant = null;

    /**
     * nss de l'enfant
     */
    public String nssEnfant = null;

    /**
     * période de la prestation
     */

    public String periode = null;
    /**
     * Prénom de l'enfant
     * 
     */
    public String prenomEnfant = null;

    /**
     * tiers bénéficiaire de la prestation
     */
    public String tiersBeneficiaire = null;

    /**
     * type de prestation
     */
    public String typePrestation = null;
    /**
     * statut de la prestation (ADC, ADI ou suisse)
     */
    private String statutPrestation = null;

    public String getStatutPrestation() {
        return statutPrestation;
    }

    public void setStatutPrestation(String statutPrestation) {
        this.statutPrestation = statutPrestation;
    }

    /**
	 * 
	 */
    public Boolean getAttestationAlloc() {
        return attestationAlloc;
    }

    /**
     * @return the dateNaissanceEnfant
     */
    public String getDateNaissanceEnfant() {
        return dateNaissanceEnfant;
    }

    /**
     * @return the dateVersement
     */
    public String getDateVersement() {
        return dateVersement;
    }

    @Override
    public String getId() {
        // DO NOTHING
        return null;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the montantDetailPrestation
     */
    public String getMontantDetailPrestation() {
        return montantDetailPrestation;
    }

    /**
     * @return the nomEnfant
     */
    public String getNomEnfant() {
        return nomEnfant;
    }

    /**
     * @return the nssEnfant
     */
    public String getNssEnfant() {
        return nssEnfant;
    }

    /**
     * @return the periode
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * @return the prenomEnfant
     */
    public String getPrenomEnfant() {
        return prenomEnfant;
    }

    @Override
    public String getSpy() {
        // DO NOTHING
        return null;
    }

    public String getTiersBeneficiaire() {
        return tiersBeneficiaire;
    }

    /**
     * @return the typePrestation
     */
    public String getTypePrestation() {
        return typePrestation;
    }

    public void setAttestationAlloc(Boolean attestationAlloc) {
        this.attestationAlloc = attestationAlloc;
    }

    /**
     * @param dateNaissanceEnfant
     *            the dateNaissanceEnfant to set
     */
    public void setDateNaissanceEnfant(String dateNaissanceEnfant) {
        this.dateNaissanceEnfant = dateNaissanceEnfant;
    }

    /**
     * @param dateVersement
     *            the dateVersement to set
     */
    public void setDateVersement(String dateVersement) {
        this.dateVersement = dateVersement;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING

    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param montantDetailPrestation
     *            the montantDetailPrestation to set
     */
    public void setMontantDetailPrestation(String montantDetailPrestation) {
        this.montantDetailPrestation = montantDetailPrestation;
    }

    /**
     * @param nomEnfant
     *            the nomEnfant to set
     */
    public void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    /**
     * @param nssEnfant
     *            the nssEnfant to set
     */
    public void setNssEnfant(String nssEnfant) {
        this.nssEnfant = nssEnfant;
    }

    /**
     * @param periode
     *            the periode to set
     */
    public void setPeriode(String periode) {
        this.periode = periode;
    }

    /**
     * @param prenomEnfant
     *            the prenomEnfant to set
     */
    public void setPrenomEnfant(String prenomEnfant) {
        this.prenomEnfant = prenomEnfant;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING

    }

    public void setTiersBeneficiaire(String tiersBeneficiaire) {
        this.tiersBeneficiaire = tiersBeneficiaire;
    }

    /**
     * @param typePrestation
     *            the typePrestation to set
     */
    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }
}
