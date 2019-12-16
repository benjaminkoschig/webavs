package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * Modèle simple de détail d'une prestation
 * 
 * @author PTA
 * 
 */
public class DetailPrestationModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Age de l'enfant au moment de la période de la validité du détail de la prestation
     */
    private String ageEnfant = null;
    /**
     * Catégorie de tarif
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
     * identifiant du détail de la prestation
     */
    private String idDetailPrestation = null;

    /**
     * Identifiant du droit
     */
    private String idDroit = null;

    /**
     * Identifiant de l'entête
     */
    private String idEntete = null;
    /**
     * Identifiant du tiers bénéficiaire
     */
    private String idTiersBeneficiaire = null;
    /**
     * Montant du détail de la prestation
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
     * Numéro du compte
     */
    private String numeroCompte = null;
    /**
     * Période de validité du détail de la prestation
     */
    private String periodeValidite = null;
    /**
     * Rang de l'enfant au moment de la génération
     */
    private String rang = null;
    /**
     * Indique si le montant de la prestation est un montant/tarif forcé
     */
    private Boolean tarifForce = null;
    /**
     * Type du détail de la prestation
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDetailPrestation;
    }

    /**
     * @return the idDetailPrestation
     */
    public String getIdDetailPrestation() {
        return idDetailPrestation;
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
     * @return Rang de l'enfant au moment de la génération
     */
    public String getRang() {
        return rang;
    }

    /**
     * @return the tarifForce
     */
    public Boolean getTarifForce() {
        return tarifForce;
    }

    /**
     * @return le type de prestation ( {@link ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE})
     */
    public String getTypePrestation() {
        return typePrestation;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDetailPrestation = id;

    }

    /**
     * @param idDetailPrestation
     *            the idDetailPrestation to set
     */
    public void setIdDetailPrestation(String idDetailPrestation) {
        this.idDetailPrestation = idDetailPrestation;
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
     *            Rang de l'enfant au moment de la génération
     */
    public void setRang(String rang) {
        this.rang = rang;
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