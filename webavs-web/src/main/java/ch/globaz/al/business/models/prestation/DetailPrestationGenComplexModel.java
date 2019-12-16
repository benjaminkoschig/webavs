package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe de détail de prestation. Il est composé du modèle simple de détail de prestation et de son en-tête
 * 
 * @author jts
 * 
 */
public class DetailPrestationGenComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Age de l'enfant au moment de la prestation
     */
    private String ageEnfant = null;
    /**
     * Code système de la catégorie de tarif
     */
    private String categorieTarif = null;
    /**
     * Catégorie de tarif pour le montant de la caisse
     */
    private String categorieTarifCaisse = null;
    /**
     * Catégorie de tarif pour le montant du canton
     */
    private String categorieTarifCanton = null;
    /**
     * date de versement de la prestation
     */
    private String dateVersement = null;

    /**
     * État de la prestation
     */
    private String etatPrestation = null;
    /**
     * id du droit
     */
    private String idDroit = null;
    /**
     * Id de l'entête
     */
    private String idEntete = null;
    /**
     * Id de la récap
     */
    private String idRecap = null;
    /**
     * Id du tiers bénéficiaire
     */
    private String idTiersBeneficiaire = null;
    /**
     * montant du détail de la prestation
     */
    private String montant = null;

    /**
     * Montant de la caisse
     */
    private String montantCaisse = null;
    /**
     * Montant cantonal
     */
    private String montantCanton = null;

    /**
     * Rubrique comptable
     */
    private String numeroCompte = null;

    /**
     * Période de validité de la prestation
     */
    private String periodeValidite = null;

    /**
     * Rang de l'enfant au moment de la génération
     */
    private String rang = null;
    /**
     * Indique si {@link DetailPrestationGenComplexModel#categorieTarif} est un tarif forcé
     */
    private Boolean tarifForce = null;
    /**
     * Code système du type de prestation
     */
    private String typePrestation = null;
    /**
     * Montant impôt à la source
     */
    private String montantIS = null;
    /**
     * Numéro du compte pour l'impôt à la source
     */
    private String numeroCompteIS = null;


    /**
     * @return the ageEnfant
     */
    public String getAgeEnfant() {
        return ageEnfant;
    }

    /**
     * @return the categorieTarif
     */
    public String getCategorieTarif() {
        return categorieTarif;
    }

    /**
     * @return the categorieTarifCaisse
     */
    public String getCategorieTarifCaisse() {
        return categorieTarifCaisse;
    }

    /**
     * @return the categorieTarifCanton
     */
    public String getCategorieTarifCanton() {
        return categorieTarifCanton;
    }

    /**
     * @return the dateVersement
     */
    public String getDateVersement() {
        return dateVersement;
    }

    /**
     * @return the etatPrestation
     */
    public String getEtatPrestation() {
        return etatPrestation;
    }

    /**
     * Retourne l'id de l'en-tête de prestation
     */
    @Override
    public String getId() {
        return idEntete;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idEntete
     */
    public String getIdEntete() {
        return idEntete;
    }

    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantCaisse
     */
    public String getMontantCaisse() {
        return montantCaisse;
    }

    /**
     * @return the montantCanton
     */
    public String getMontantCanton() {
        return montantCanton;
    }

    /**
     * @return the numeroCompte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * @return the periodeValidite
     */
    public String getPeriodeValidite() {
        return periodeValidite;
    }

    /**
     * @return the rang
     */
    public String getRang() {
        return rang;
    }

    /**
     * retourne <code>null</code>
     */
    @Override
    public String getSpy() {
        // DO NOTHING
        return null;
    }

    /**
     * @return the montantIS
     */
    public String getMontantIS() {
        return montantIS;
    }
    /**
     * @return the numeroCompteIS
     */
    public String getNumeroCompteIS() {
        return numeroCompteIS;
    }

    /**
     * Indique si {@link DetailPrestationGenComplexModel#categorieTarif} est un tarif forcé
     * 
     * @return <code>true</code> si le tarif est forcé, sinon <code>false</code>
     */
    public Boolean getTarifForce() {
        return tarifForce;
    }

    /**
     * @return the typePrestation
     */
    public String getTypePrestation() {
        return typePrestation;
    }

    /**
     * @param ageEnfant
     *            the ageEnfant to set
     */
    public void setAgeEnfant(String ageEnfant) {
        this.ageEnfant = ageEnfant;
    }

    /**
     * @param categorieTarif
     *            the categorieTarif to set
     */
    public void setCategorieTarif(String categorieTarif) {
        this.categorieTarif = categorieTarif;
    }

    /**
     * @param categorieTarifCaisse
     *            the categorieTarifCaisse to set
     */
    public void setCategorieTarifCaisse(String categorieTarifCaisse) {
        this.categorieTarifCaisse = categorieTarifCaisse;
    }

    /**
     * @param categorieTarifCanton
     *            the categorieTarifCanton to set
     */
    public void setCategorieTarifCanton(String categorieTarifCanton) {
        this.categorieTarifCanton = categorieTarifCanton;
    }

    /**
     * @param dateVersement
     *            the dateVersement to set
     */
    public void setDateVersement(String dateVersement) {
        this.dateVersement = dateVersement;
    }

    /**
     * @param etatPrestation
     *            the etatPrestation to set
     */
    public void setEtatPrestation(String etatPrestation) {
        this.etatPrestation = etatPrestation;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idEntete
     *            the idEntete to set
     */
    public void setIdEntete(String idEntete) {
        this.idEntete = idEntete;
    }

    public void setIdRecap(String idRecap) {
        this.idRecap = idRecap;
    }

    /**
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantCaisse
     *            the montantCaisse to set
     */
    public void setMontantCaisse(String montantCaisse) {
        this.montantCaisse = montantCaisse;
    }

    /**
     * @param montantCanton
     *            the montantCanton to set
     */
    public void setMontantCanton(String montantCanton) {
        this.montantCanton = montantCanton;
    }

    /**
     * @param numeroCompte
     *            the numeroCompte to set
     */
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    /**
     * @param periodeValidite
     *            the periodeValidite to set
     */
    public void setPeriodeValidite(String periodeValidite) {
        this.periodeValidite = periodeValidite;
    }

    /**
     * @param rang
     *            the rang to set
     */
    public void setRang(String rang) {
        this.rang = rang;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }

    /**
     * @param tarifForce
     *            the tarifForce to set
     */
    public void setTarifForce(Boolean tarifForce) {
        this.tarifForce = tarifForce;
    }

    /**
     * @param typePrestation
     *            the typePrestation to set
     */
    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    /**
     * @param montantIS
     *            the montantIS to set
     */
    public void setMontantIS(String montantIS) {
        this.montantIS = montantIS;
    }

    /**
     * @param numeroCompteIS
     *            the numeroCompteIS to set
     */
    public void setNumeroCompteIS(String numeroCompteIS) {
        this.numeroCompteIS = numeroCompteIS;
    }
}
