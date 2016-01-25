package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleNumeraire extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePropriete = null;
    private String idDonneeFinanciereHeader = null;
    private String idNumeraire = null;
    private Boolean isSansInteret = Boolean.TRUE;
    private String montant = null;
    private String montantInteret = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;

    /**
     * @return the csTypePropriete
     */
    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idNumeraire;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idNumeraire
     */
    public String getIdNumeraire() {
        return idNumeraire;
    }

    /**
     * @return the isSansInteret
     */
    public Boolean getIsSansInteret() {
        return isSansInteret;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantInteret
     */
    public String getMontantInteret() {
        return montantInteret;
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
     * @param csTypePropriete
     *            the csTypePropriete to set
     */
    public void setCsTypePropriete(String csTypePropriete) {
        this.csTypePropriete = csTypePropriete;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idNumeraire = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idNumeraire
     *            the idNumeraire to set
     */
    public void setIdNumeraire(String idNumeraire) {
        this.idNumeraire = idNumeraire;
    }

    /**
     * @param isSansInteret
     *            the isSansInteret to set
     */
    public void setIsSansInteret(Boolean isSansInteret) {
        this.isSansInteret = isSansInteret;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantInteret
     *            the montantInteret to set
     */
    public void setMontantInteret(String montantInteret) {
        this.montantInteret = montantInteret;
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

}
