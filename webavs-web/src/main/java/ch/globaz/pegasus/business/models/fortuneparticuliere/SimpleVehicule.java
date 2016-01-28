package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleVehicule extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePropriete = null;
    private String designation = null;
    private String idDonneeFinanciereHeader = null;
    private String idVehicule = null;
    private String montant = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;

    /**
     * @return the csTypePropriete
     */
    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idVehicule;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idVehicule
     */
    public String getIdVehicule() {
        return idVehicule;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
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

    /**
     * @param designation
     *            the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idVehicule = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idVehicule
     *            the idVehicule to set
     */
    public void setIdVehicule(String idVehicule) {
        this.idVehicule = idVehicule;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
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
