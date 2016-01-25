package ch.globaz.pegasus.business.models.dessaisissement;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDessaisissementFortune extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreMotifDessaisissement = null;
    private String charges = null;
    private String csMotifDessaisissement = null;

    private String dateValeur = null;
    private String deductionMontantDessaisi = null;
    private String idDessaisissementFortune = null;

    private String idDonneeFinanciereHeader = null;

    private Boolean isContrePrestation = null;

    private String montantBrut = null;

    private String rendementFortune = null;

    /**
     * @return the autreMotifDessaisissement
     */
    public String getAutreMotifDessaisissement() {
        return autreMotifDessaisissement;
    }

    /**
     * @return the charges
     */
    public String getCharges() {
        return charges;
    }

    /**
     * @return the csMotifDessaisissement
     */
    public String getCsMotifDessaisissement() {
        return csMotifDessaisissement;
    }

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
        return idDessaisissementFortune;
    }

    /**
     * @return the idDessaisissementFortune
     */
    public String getIdDessaisissementFortune() {
        return idDessaisissementFortune;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the isContrePrestation
     */
    public Boolean getIsContrePrestation() {
        return isContrePrestation;
    }

    /**
     * @return the montantBrut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * @return the rendementFortune
     */
    public String getRendementFortune() {
        return rendementFortune;
    }

    /**
     * @param autreMotifDessaisissement
     *            the autreMotifDessaisissement to set
     */
    public void setAutreMotifDessaisissement(String autreMotifDessaisissement) {
        this.autreMotifDessaisissement = autreMotifDessaisissement;
    }

    /**
     * @param charges
     *            the charges to set
     */
    public void setCharges(String charges) {
        this.charges = charges;
    }

    /**
     * @param csMotifDessaisissement
     *            the csMotifDessaisissement to set
     */
    public void setCsMotifDessaisissement(String csMotifDessaisissement) {
        this.csMotifDessaisissement = csMotifDessaisissement;
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
        idDessaisissementFortune = id;

    }

    /**
     * @param idDessaisissementFortune
     *            the idDessaisissementFortune to set
     */
    public void setIdDessaisissementFortune(String idDessaisissementFortune) {
        this.idDessaisissementFortune = idDessaisissementFortune;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param isContrePrestation
     *            the isContrePrestation to set
     */
    public void setIsContrePrestation(Boolean isContrePrestation) {
        this.isContrePrestation = isContrePrestation;
    }

    /**
     * @param montantBrut
     *            the montantBrut to set
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    /**
     * @param rendementFortune
     *            the rendementFortune to set
     */
    public void setRendementFortune(String rendementFortune) {
        this.rendementFortune = rendementFortune;
    }

}
