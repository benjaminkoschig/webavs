package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePlanDeCalcul extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatPC = null;
    private String excedentPCAnnuel = null;
    private String idPCAccordee = null;
    private String idPlanDeCalcul = null;
    private Boolean isPlanCalculAccessible = Boolean.FALSE;
    private Boolean isPlanRetenu = Boolean.FALSE;
    private Boolean isPlanNonRetenu = Boolean.FALSE;
    private String montantPCMensuelle = null;
    private String primeMoyenneAssMaladie = null;
    private String primeVerseeAssMaladie = null;
    private String montantPrixHome = null;
    private String montantPartCantonale = null;


    private byte[] resultatCalcul = null;

    private Boolean reformePc = false;

    public String getEtatPC() {
        return etatPC;
    }

    public String getExcedentPCAnnuel() {
        return excedentPCAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPlanDeCalcul;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdPlanDeCalcul() {
        return idPlanDeCalcul;
    }

    public Boolean getIsPlanCalculAccessible() {
        return isPlanCalculAccessible;
    }

    public Boolean getIsPlanRetenu() {
        return isPlanRetenu;
    }

    public Boolean getIsPlanNonRetenu() {
        return isPlanNonRetenu;
    }

    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    public String getPrimeMoyenneAssMaladie() {
        return primeMoyenneAssMaladie;
    }

    /**
     * @return the resultatCalcul
     */
    public byte[] getResultatCalcul() {
        return resultatCalcul;
    }

    public void setEtatPC(String etatPC) {
        this.etatPC = etatPC;
    }

    public void setExcedentPCAnnuel(String excedantPCAnnuel) {
        excedentPCAnnuel = excedantPCAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPlanDeCalcul = id;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdPlanDeCalcul(String idPlanDeCalcul) {
        this.idPlanDeCalcul = idPlanDeCalcul;
    }

    public void setIsPlanCalculAccessible(Boolean isPlanCalculAccessible) {
        this.isPlanCalculAccessible = isPlanCalculAccessible;
    }

    public void setIsPlanRetenu(Boolean isPlanRetenu) {
        this.isPlanRetenu = isPlanRetenu;
    }

    public void setIsPlanNonRetenu(Boolean isPlanNonRetenu) {
        this.isPlanNonRetenu = isPlanNonRetenu;
    }

    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }

    public void setPrimeMoyenneAssMaladie(String primeMoyenneAssMaladie) {
        this.primeMoyenneAssMaladie = primeMoyenneAssMaladie;
    }

    /**
     * @param resultatCalcul
     *            the resultatCalcul to set
     */
    public void setResultatCalcul(byte[] resultatCalcul) {
        this.resultatCalcul = resultatCalcul;
    }

    public Boolean getReformePc() {
        return reformePc;
    }

    public void setReformePc(Boolean reformePc) {
        this.reformePc = reformePc;
    }

    public String getMontantPrixHome() {
        return montantPrixHome;
    }

    public void setMontantPrixHome(String montantPrixHome) {
        this.montantPrixHome = montantPrixHome;
    }

    public String getPrimeVerseeAssMaladie() {
        return primeVerseeAssMaladie;
    }

    public void setPrimeVerseeAssMaladie(String primeVerseeAssMaladie) {
        this.primeVerseeAssMaladie = primeVerseeAssMaladie;
    }

    public String getMontantPartCantonale() {
        return montantPartCantonale;
    }

    public void setMontantPartCantonale(String montantPartCantonale) {
        this.montantPartCantonale = montantPartCantonale;
    }
}
