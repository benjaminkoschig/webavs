package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleAutreFortuneMobiliere extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autre = null;
    private String csTypeFortune = null;
    private String csTypePropriete = null;
    private String idAutreFortuneMobiliere = null;
    private String idDonneeFinanciereHeader = null;
    private String montant = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;

    public String getAutre() {
        return autre;
    }

    /**
     * @return the csTypeFortune
     */
    public String getCsTypeFortune() {
        return csTypeFortune;
    }

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
        return idAutreFortuneMobiliere;
    }

    /**
     * @return the idAutresFortunesMobilieres
     */
    public String getIdAutreFortuneMobiliere() {
        return idAutreFortuneMobiliere;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
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

    public void setAutre(String autre) {
        this.autre = autre;
    }

    /**
     * @param csTypeFortune
     *            the csTypeFortune to set
     */
    public void setCsTypeFortune(String csTypeFortune) {
        this.csTypeFortune = csTypeFortune;
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
        idAutreFortuneMobiliere = id;
    }

    /**
     * @param idAutreFortuneMobiliere
     *            the idAutreFortuneMobiliere to set
     */
    public void setIdAutreFortuneMobiliere(String idAutreFortuneMobiliere) {
        this.idAutreFortuneMobiliere = idAutreFortuneMobiliere;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
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
