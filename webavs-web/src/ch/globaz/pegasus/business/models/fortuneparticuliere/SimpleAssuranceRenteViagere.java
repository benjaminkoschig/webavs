package ch.globaz.pegasus.business.models.fortuneparticuliere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleAssuranceRenteViagere extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String excedentRenteViagere = null;
    private String idAssuranceRenteViagere = null;
    private String idCompagnie = null;
    private String idDonneeFinanciereHeader = null;
    private String montantRenteViagere = null;
    private String montantValeurRachat = null;
    private String numeroPolice = null;

    /**
     * @return the excedentRenteViagere
     */
    public String getExcedentRenteViagere() {
        return excedentRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idAssuranceRenteViagere;
    }

    /**
     * @return the idAssuranceRenteViagere
     */
    public String getIdAssuranceRenteViagere() {
        return idAssuranceRenteViagere;
    }

    /**
     * @return the idCompagnie
     */
    public String getIdCompagnie() {
        return idCompagnie;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the montantRenteViagere
     */
    public String getMontantRenteViagere() {
        return montantRenteViagere;
    }

    /**
     * @return the montantValeurRachat
     */
    public String getMontantValeurRachat() {
        return montantValeurRachat;
    }

    /**
     * @return the numeroPolice
     */
    public String getNumeroPolice() {
        return numeroPolice;
    }

    /**
     * @param excedentRenteViagere
     *            the excedentRenteViagere to set
     */
    public void setExcedentRenteViagere(String excedentRenteViagere) {
        this.excedentRenteViagere = excedentRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAssuranceRenteViagere = id;
    }

    /**
     * @param idAssuranceRenteViagere
     *            the idAssuranceRenteViagere to set
     */
    public void setIdAssuranceRenteViagere(String idAssuranceRenteViagere) {
        this.idAssuranceRenteViagere = idAssuranceRenteViagere;
    }

    /**
     * @param idCompagnie
     *            the idCompagnie to set
     */
    public void setIdCompagnie(String idCompagnie) {
        this.idCompagnie = idCompagnie;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param montantRenteViagere
     *            the montantRenteViagere to set
     */
    public void setMontantRenteViagere(String montantRenteViagere) {
        this.montantRenteViagere = montantRenteViagere;
    }

    /**
     * @param montantValeurRachat
     *            the montantValeurRachat to set
     */
    public void setMontantValeurRachat(String montantValeurRachat) {
        this.montantValeurRachat = montantValeurRachat;
    }

    /**
     * @param numeroPolice
     *            the numeroPolice to set
     */
    public void setNumeroPolice(String numeroPolice) {
        this.numeroPolice = numeroPolice;
    }

}
