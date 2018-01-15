/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import java.util.List;

/**
 * @author LBE
 * 
 */
public class AnnonceSedexCO2Detail {
    private AnnonceSedexCO2AssureContainer debiteur;
    private List<AnnonceSedexCO2AssureContainer> assureList;
    private Boolean isCreance;
    private String idAnnonceCO;
    private String idSedex;
    private String anneeAnnonce;
    private String status;
    private String daAnnonce;
    private String caisseMaladie;
    private String libelleAnnonce;
    private String interets;
    private String frais;
    private String totalCreance;

    /**
     * Default constructor
     */
    public AnnonceSedexCO2Detail() {
        isCreance = false;
        anneeAnnonce = "";
        daAnnonce = "";
        caisseMaladie = "";
        libelleAnnonce = "";
        interets = "";
        frais = "";
        totalCreance = "";
    }

    /**
     * @return the isCreance
     */
    public Boolean getIsCreance() {
        return isCreance;
    }

    /**
     * @param isCreance the isCreance to set
     */
    public void setIsCreance(Boolean isCreance) {
        this.isCreance = isCreance;
    }

    /**
     * @return the idAnnonceCO
     */
    public String getIdAnnonceCO() {
        return idAnnonceCO;
    }

    /**
     * @param idAnnonceCO the idAnnonceCO to set
     */
    public void setIdAnnonceCO(String idAnnonceCO) {
        this.idAnnonceCO = idAnnonceCO;
    }

    /**
     * @return the idSedex
     */
    public String getIdSedex() {
        return idSedex;
    }

    /**
     * @param idSedex the idSedex to set
     */
    public void setIdSedex(String idSedex) {
        this.idSedex = idSedex;
    }

    /**
     * @return the débiteur
     */
    public AnnonceSedexCO2AssureContainer getDebiteur() {
        return debiteur;
    }

    /**
     * @param debiteur the débiteur to set
     */
    public void setDebiteur(AnnonceSedexCO2AssureContainer debiteur) {
        this.debiteur = debiteur;
    }

    /**
     * @return the assureList
     */
    public List<AnnonceSedexCO2AssureContainer> getAssureList() {
        return assureList;
    }

    /**
     * @param assureList the assureList to set
     */
    public void setAssureList(List<AnnonceSedexCO2AssureContainer> assureList) {
        this.assureList = assureList;
    }

    /**
     * @return the anneeAnnonce
     */
    public String getAnneeAnnonce() {
        return anneeAnnonce;
    }

    /**
     * @param anneeAnnonce the anneeAnnonce to set
     */
    public void setAnneeAnnonce(String anneeAnnonce) {
        this.anneeAnnonce = anneeAnnonce;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the daAnnonce
     */
    public String getDaAnnonce() {
        return daAnnonce;
    }

    /**
     * @param daAnnonce the daAnnonce to set
     */
    public void setDaAnnonce(String daAnnonce) {
        this.daAnnonce = daAnnonce;
    }

    /**
     * @return the caisseMaladie
     */
    public String getCaisseMaladie() {
        return caisseMaladie;
    }

    /**
     * @param caisseMaladie the caisseMaladie to set
     */
    public void setCaisseMaladie(String caisseMaladie) {
        this.caisseMaladie = caisseMaladie;
    }

    /**
     * @return the libelleAnnonce
     */
    public String getLibelleAnnonce() {
        return libelleAnnonce;
    }

    /**
     * @param libelleAnnonce the libelleAnnonce to set
     */
    public void setLibelleAnnonce(String libelleAnnonce) {
        this.libelleAnnonce = libelleAnnonce;
    }

    /**
     * @return the intérêts
     */
    public String getInterets() {
        return interets;
    }

    /**
     * @param interets the intérêts to set
     */
    public void setInterets(String interets) {
        this.interets = interets;
    }

    /**
     * @return the frais
     */
    public String getFrais() {
        return frais;
    }

    /**
     * @param frais the frais to set
     */
    public void setFrais(String frais) {
        this.frais = frais;
    }

    /**
     * @return the totalCreance
     */
    public String getTotalCreance() {
        return totalCreance;
    }

    /**
     * @param totalCreance the totalCreance to set
     */
    public void setTotalCreance(String totalCreance) {
        this.totalCreance = totalCreance;
    }

}
