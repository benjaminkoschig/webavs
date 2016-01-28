package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleMarchandisesStock extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePropriete = null;
    private String idDonneeFinanciereHeader = null;
    private String idMarchandisesStock = null;
    private String montantStock = null;
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
        return idMarchandisesStock;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idMarchandisesStock
     */
    public String getIdMarchandisesStock() {
        return idMarchandisesStock;
    }

    /**
     * @return the montantStock
     */
    public String getMontantStock() {
        return montantStock;
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
        idMarchandisesStock = id;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idMarchandisesStock
     *            the idMarchandisesStock to set
     */
    public void setIdMarchandisesStock(String idMarchandisesStock) {
        this.idMarchandisesStock = idMarchandisesStock;
    }

    /**
     * @param montantStock
     *            the montantStock to set
     */
    public void setMontantStock(String montantStock) {
        this.montantStock = montantStock;
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
