package ch.globaz.pegasus.business.models.dessaisissement;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDessaisissementRevenu extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateValeur = null;
    private String deductionMontantDessaisi = null;
    private String idDessaisissementRevenu = null;
    private String idDonneeFinanciereHeader = null;
    private String libelleDessaisissement = null;
    private String montantBrut = null;

    /**
     * @return the dateValeur
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * @return the deductionMontantDessaisi
     */
    public String getDeductionMontantDessaisi() {
        return deductionMontantDessaisi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDessaisissementRevenu;
    }

    /**
     * @return the idDessaisissementRevenu
     */
    public String getIdDessaisissementRevenu() {
        return idDessaisissementRevenu;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the libelleDessaisissement
     */
    public String getLibelleDessaisissement() {
        return libelleDessaisissement;
    }

    /**
     * @return the montantBrut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * @param dateValeur
     *            the dateValeur to set
     */
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * @param deductionMontantDessaisi
     *            the deductionMontantDessaisi to set
     */
    public void setDeductionMontantDessaisi(String deductionMontantDessaisi) {
        this.deductionMontantDessaisi = deductionMontantDessaisi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDessaisissementRevenu = id;

    }

    /**
     * @param idDessaisissementRevenu
     *            the idDessaisissementRevenu to set
     */
    public void setIdDessaisissementRevenu(String idDessaisissementRevenu) {
        this.idDessaisissementRevenu = idDessaisissementRevenu;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param libelleDessaisissement
     *            the libelleDessaisissement to set
     */
    public void setLibelleDessaisissement(String libelleDessaisissement) {
        this.libelleDessaisissement = libelleDessaisissement;
    }

    /**
     * @param montantBrut
     *            the montantBrut to set
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

}
