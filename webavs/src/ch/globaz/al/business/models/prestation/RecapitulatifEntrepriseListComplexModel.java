package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeComplexModel;

public class RecapitulatifEntrepriseListComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String bonification = null;
    private String etat = null;
    private String nomProcessus = null;
    private String numeroAffilie = null;
    private String numeroFacture = null;
    private String numeroProcessus = null;
    private String numeroRecap = null;
    private String periodeA = null;
    private String periodeDe = null;
    private String periodeProcessus = null;
    /**
     * rootModel
     */
    private RecapitulatifEntrepriseModel recapModel = null;

    /**
     * bonification
     * 
     * @return
     */
    public String getBonification() {
        return bonification;
    }

    /**
     * 
     * @return etat
     */
    public String getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return numeroRecap;
    }

    /**
     * 
     * @return nomProcessus
     */
    public String getNomProcessus() {
        return nomProcessus;
    }

    /**
     * 
     * @return numeroAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * 
     * @return numeroFacture
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * 
     * @return numeroProcessus
     */
    public String getNumeroProcessus() {
        return numeroProcessus;
    }

    /**
     * 
     * @return numeroRecap
     */
    public String getNumeroRecap() {
        return numeroRecap;
    }

    /**
     * 
     * @return periodeA
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * 
     * @return periodeDe
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /**
     * 
     * @return periodeProcessus
     */
    public String getPeriodeProcessus() {
        return periodeProcessus;
    }

    /**
     * 
     * @return recapModel
     */
    public RecapitulatifEntrepriseModel getRecapModel() {
        return recapModel;
    }

    @Override
    public String getSpy() {
        return null;
    }

    /**
     * 
     * @param bonification
     */
    public void setBonification(String bonification) {
        this.bonification = bonification;
    }

    /**
     * 
     * @param etat
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String id) {
        numeroRecap = id;
    }

    /**
     * 
     * @param nomProcessus
     */
    public void setNomProcessus(String nomProcessus) {
        this.nomProcessus = nomProcessus;
    }

    /**
     * 
     * @param numeroAffilie
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * 
     * @param numeroFacture
     */
    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    /**
     * 
     * @param numeroProcessus
     */
    public void setNumeroProcessus(String numeroProcessus) {
        this.numeroProcessus = numeroProcessus;
    }

    /**
     * 
     * @param numeroRecap
     */
    public void setNumeroRecap(String numeroRecap) {
        this.numeroRecap = numeroRecap;
    }

    /**
     * 
     * @param periodeA
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * 
     * @param periodeDe
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

    /**
     * 
     * @param periodeProcessus
     */
    public void setPeriodeProcessus(String periodeProcessus) {
        this.periodeProcessus = periodeProcessus;
    }

    /**
     * 
     * @param recapModel
     */
    public void setRecapModel(RecapitulatifEntrepriseModel recapModel) {
        this.recapModel = recapModel;
    }

    @Override
    public void setSpy(String spy) {

    }

}
