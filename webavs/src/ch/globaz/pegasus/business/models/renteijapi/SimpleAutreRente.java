package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAutreRente extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreGenre = null;
    private String csGenre = null;
    private String csMonnaie = null;
    private String csType = null;
    private String dateEcheance = null;
    private String fournisseurPrestation = null;
    private String idAutreRente = null;
    private String idDonneeFinanciereHeader = null;
    private String idPays = null;
    // private String idFournisseurPrestation = null;
    private String montant = null;

    /**
     * @return the autreGenre
     */
    public String getAutreGenre() {
        return autreGenre;
    }

    /**
     * @return the csGenre
     */
    public String getCsGenre() {
        return csGenre;
    }

    /**
     * @return the csMonnaie
     */
    public String getCsMonnaie() {
        return csMonnaie;
    }

    /**
     * @return the csType
     */
    public String getCsType() {
        return csType;
    }

    /**
     * @return the dateEcheance
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * @return the fournisseurPrestation
     */
    public String getFournisseurPrestation() {
        return fournisseurPrestation;
    }

    @Override
    public String getId() {
        return idAutreRente;
    }

    /**
     * @return the idAutreRente
     */
    public String getIdAutreRente() {
        return idAutreRente;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idPays
     */
    public String getIdPays() {
        return idPays;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param autreGenre
     *            the autreGenre to set
     */
    public void setAutreGenre(String autreGenre) {
        this.autreGenre = autreGenre;
    }

    /**
     * @param csGenre
     *            the csGenre to set
     */
    public void setCsGenre(String csGenre) {
        this.csGenre = csGenre;
    }

    /**
     * @param csMonnaie
     *            the csMonnaie to set
     */
    public void setCsMonnaie(String csMonnaie) {
        this.csMonnaie = csMonnaie;
    }

    /**
     * @param csType
     *            the csType to set
     */
    public void setCsType(String csType) {
        this.csType = csType;
    }

    /**
     * @param dateEcheance
     *            the dateEcheance to set
     */
    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * @param fournisseurPrestation
     *            the fournisseurPrestation to set
     */
    public void setFournisseurPrestation(String fournisseurPrestation) {
        this.fournisseurPrestation = fournisseurPrestation;
    }

    @Override
    public void setId(String id) {
        idAutreRente = id;

    }

    /**
     * @param idAutreRente
     *            the idAutreRente to set
     */
    public void setIdAutreRente(String idAutreRente) {
        this.idAutreRente = idAutreRente;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idPays
     *            the idPays to set
     */
    public void setIdPays(String idPays) {
        this.idPays = idPays;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

}
