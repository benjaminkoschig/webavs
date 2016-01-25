package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DMA
 * 
 */
public class SimpleIjApg extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreGenrePresation = null;
    private String cotisationLPPMens = null;
    private String csGenrePrestation = null;
    private String gainIntAnnuel = null;
    private String idDonneeFinanciereHeader = null;
    private String idFournisseurPrestation = null;
    private String idIjApg = null;
    private String montant = null;
    private String montantBrutAC = null;
    private String nbJours = null;
    private String tauxAA = null;
    private String tauxAVS = null;

    /**
     * @return the autreGenrePresation
     */
    public String getAutreGenrePresation() {
        return autreGenrePresation;
    }

    /**
     * @return the cotisationLPPMens
     */
    public String getCotisationLPPMens() {
        return cotisationLPPMens;
    }

    /**
     * @return the csGenrePrestation
     */
    public String getCsGenrePrestation() {
        return csGenrePrestation;
    }

    /**
     * @return the gainIntAnnuel
     */
    public String getGainIntAnnuel() {
        return gainIntAnnuel;
    }

    @Override
    public String getId() {
        return idIjApg;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idFournisseurPrestation
     */
    public String getIdFournisseurPrestation() {
        return idFournisseurPrestation;
    }

    /**
     * @return the idIjApg
     */
    public String getIdIjApg() {
        return idIjApg;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantBrutAC
     */
    public String getMontantBrutAC() {
        return montantBrutAC;
    }

    /**
     * @return the nbJours
     */
    public String getNbJours() {
        return nbJours;
    }

    /**
     * @return the tauxAA
     */
    public String getTauxAA() {
        return tauxAA;
    }

    /**
     * @return the tauxAVS
     */
    public String getTauxAVS() {
        return tauxAVS;
    }

    /**
     * @param autreGenrePresation
     *            the autreGenrePresation to set
     */
    public void setAutreGenrePresation(String autreGenrePresation) {
        this.autreGenrePresation = autreGenrePresation;
    }

    /**
     * @param cotisationLPPMens
     *            the cotisationLPPMens to set
     */
    public void setCotisationLPPMens(String cotisationLPPMens) {
        this.cotisationLPPMens = cotisationLPPMens;
    }

    /**
     * @param csGenrePrestation
     *            the csGenrePrestation to set
     */
    public void setCsGenrePrestation(String csGenrePrestation) {
        this.csGenrePrestation = csGenrePrestation;
    }

    /**
     * @param gainIntAnnuel
     *            the gainIntAnnuel to set
     */
    public void setGainIntAnnuel(String gainIntAnnuel) {
        this.gainIntAnnuel = gainIntAnnuel;
    }

    @Override
    public void setId(String id) {
        idIjApg = id;

    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idFournisseurPrestation
     *            the idFournisseurPrestation to set
     */
    public void setIdFournisseurPrestation(String idFournisseurPrestation) {
        this.idFournisseurPrestation = idFournisseurPrestation;
    }

    /**
     * @param idIjApg
     *            the idIjApg to set
     */
    public void setIdIjApg(String idIjApg) {
        this.idIjApg = idIjApg;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantBrutAC
     *            the montantBrutAC to set
     */
    public void setMontantBrutAC(String montantBrutAC) {
        this.montantBrutAC = montantBrutAC;
    }

    /**
     * @param nbJours
     *            the nbJours to set
     */
    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    /**
     * @param tauxAA
     *            the tauxAA to set
     */
    public void setTauxAA(String tauxAA) {
        this.tauxAA = tauxAA;
    }

    /**
     * @param tauxAVS
     *            the tauxAVS to set
     */
    public void setTauxAVS(String tauxAVS) {
        this.tauxAVS = tauxAVS;
    }
}
