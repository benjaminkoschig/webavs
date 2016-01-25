package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author BSC
 * 
 */
public class SimplePretEnversTiers extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePropriete = null;
    private String dateEcheance = null;
    private String idDonneeFinanciereHeader = null;
    private String idPretEnversTiers = null;
    private Boolean isSansInteret = Boolean.TRUE;
    private String montantInteret = null;
    private String montantPret = null;
    private String nomPrenomBeneficiaire = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;
    private String typePret = null;

    /**
     * @return the csTypePropriete
     */
    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    /**
     * @return the dateEcheance
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPretEnversTiers;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idPretEnversTiers
     */
    public String getIdPretEnversTiers() {
        return idPretEnversTiers;
    }

    /**
     * @return the isSansInteret
     */
    public Boolean getIsSansInteret() {
        return isSansInteret;
    }

    /**
     * @return the montantInteret
     */
    public String getMontantInteret() {
        return montantInteret;
    }

    /**
     * @return the montantPret
     */
    public String getMontantPret() {
        return montantPret;
    }

    /**
     * @return the nomPrenomBeneficiaire
     */
    public String getNomPrenomBeneficiaire() {
        return nomPrenomBeneficiaire;
    }

    /**
     * @return the partProprieteDenominateur
     */
    public String getPartProprieteDenominateur() {
        return partProprieteDenominateur;
    }

    /**
     * @return the partProprieteNumerateur
     */
    public String getPartProprieteNumerateur() {
        return partProprieteNumerateur;
    }

    /**
     * @return the csTypePret
     */
    public String getTypePret() {
        return typePret;
    }

    /**
     * @param csTypePropriete
     *            the csTypePropriete to set
     */
    public void setCsTypePropriete(String csTypePropriete) {
        this.csTypePropriete = csTypePropriete;
    }

    /**
     * @param dateEcheance
     *            the dateEcheance to set
     */
    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPretEnversTiers = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idPretEnversTiers
     *            the idPretEnversTiers to set
     */
    public void setIdPretEnversTiers(String idPretEnversTiers) {
        this.idPretEnversTiers = idPretEnversTiers;
    }

    /**
     * @param isSansInteret
     *            the isSansInteret to set
     */
    public void setIsSansInteret(Boolean isSansInteret) {
        this.isSansInteret = isSansInteret;
    }

    /**
     * @param montantInteret
     *            the montantInteret to set
     */
    public void setMontantInteret(String montantInteret) {
        this.montantInteret = montantInteret;
    }

    /**
     * @param montantPret
     *            the montantPret to set
     */
    public void setMontantPret(String montantPret) {
        this.montantPret = montantPret;
    }

    /**
     * @param nomPrenomBeneficiaire
     *            the nomPrenomBeneficiaire to set
     */
    public void setNomPrenomBeneficiaire(String nomPrenomBeneficiaire) {
        this.nomPrenomBeneficiaire = nomPrenomBeneficiaire;
    }

    /**
     * @param partProprieteDenominateur
     *            the partProprieteDenominateur to set
     */
    public void setPartProprieteDenominateur(String partProprieteDenominateur) {
        this.partProprieteDenominateur = partProprieteDenominateur;
    }

    /**
     * @param partProprieteNumerateur
     *            the partProprieteNumerateur to set
     */
    public void setPartProprieteNumerateur(String partProprieteNumerateur) {
        this.partProprieteNumerateur = partProprieteNumerateur;
    }

    /**
     * @param typePret
     *            the typePret to set
     */
    public void setTypePret(String typePret) {
        this.typePret = typePret;
    }

}
