package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRevenuContribuable extends JadeSimpleModel implements Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String allocationFamiliale = null;
    private String deducAppEtu = null;
    private String excDepSuccNp = null;
    private String excedDepPropImmoComm = null;
    private String excedDepPropImmoPriv = null;
    private String fortuneImposable = null;
    private String fortuneTaux = null;
    private String idRevenu = null;
    private String idRevenuContribuable = null;
    private String indemniteImposable = null;
    private String interetsPassifsCalcul = null;
    private String interetsPassifsComm = null;
    private String interetsPassifsPrive = null;
    private String persChargeEnf = null;
    private String perteActAccInd = null;
    private String perteActAgricole = null;
    private String perteActIndep = null;
    private String perteCommercial = null;
    private String perteExercicesComm = null;
    private String perteLiquidation = null;
    private String perteSociete = null;
    private String rendFortImmobComm = null;
    private String rendFortImmobPrive = null;
    private String revenuImposable = null;
    private String revenuNetEmploi = null;
    private String revenuNetEpouse = null;
    private String revenuTaux = null;
    private String totalRevenusNets = null;
    private String deductionCouplesMaries = null;

    @Override
    public SimpleRevenuContribuable clone() throws CloneNotSupportedException {
        return (SimpleRevenuContribuable) super.clone();
    }

    /**
     * @return the allocationFamiliale
     */
    public String getAllocationFamiliale() {
        return allocationFamiliale;
    }

    /**
     * @return the deducAppEtu
     */
    public String getDeducAppEtu() {
        return deducAppEtu;
    }

    /**
     * @return the excDepSuccNp
     */
    public String getExcDepSuccNp() {
        return excDepSuccNp;
    }

    /**
     * @return the excedDepPropImmoComm
     */
    public String getExcedDepPropImmoComm() {
        return excedDepPropImmoComm;
    }

    /**
     * @return the excedDepPropImmoPriv
     */
    public String getExcedDepPropImmoPriv() {
        return excedDepPropImmoPriv;
    }

    /**
     * @return the fortuneImposable
     */
    public String getFortuneImposable() {
        return fortuneImposable;
    }

    /**
     * @return the fortuneTaux
     */
    public String getFortuneTaux() {
        return fortuneTaux;
    }

    @Override
    public String getId() {
        return idRevenuContribuable;
    }

    public String getIdRevenu() {
        return idRevenu;
    }

    public String getIdRevenuContribuable() {
        return idRevenuContribuable;
    }

    /**
     * @return the indemniteImposable
     */
    public String getIndemniteImposable() {
        return indemniteImposable;
    }

    public String getInteretsPassifsCalcul() {
        return interetsPassifsCalcul;
    }

    /**
     * @return the interetsPassifsComm
     */
    public String getInteretsPassifsComm() {
        return interetsPassifsComm;
    }

    /**
     * @return the interetsPassifsPrive
     */
    public String getInteretsPassifsPrive() {
        return interetsPassifsPrive;
    }

    /**
     * @return the persChargeEnf
     */
    public String getPersChargeEnf() {
        return persChargeEnf;
    }

    /**
     * @return the perteActAccInd
     */
    public String getPerteActAccInd() {
        return perteActAccInd;
    }

    /**
     * @return the perteActAgricole
     */
    public String getPerteActAgricole() {
        return perteActAgricole;
    }

    /**
     * @return the perteActIndep
     */
    public String getPerteActIndep() {
        return perteActIndep;
    }

    /**
     * @return the perteCommercial
     */
    public String getPerteCommercial() {
        return perteCommercial;
    }

    public String getPerteExercicesComm() {
        return perteExercicesComm;
    }

    /**
     * @return the perteLiquidation
     */
    public String getPerteLiquidation() {
        return perteLiquidation;
    }

    /**
     * @return the perteSociete
     */
    public String getPerteSociete() {
        return perteSociete;
    }

    /**
     * @return the rendFortImmobComm
     */
    public String getRendFortImmobComm() {
        return rendFortImmobComm;
    }

    /**
     * @return the rendFortImmobPrive
     */
    public String getRendFortImmobPrive() {
        return rendFortImmobPrive;
    }

    /**
     * @return the revenuImposable
     */
    public String getRevenuImposable() {
        return revenuImposable;
    }

    /**
     * @return the revenuNetEmploi
     */
    public String getRevenuNetEmploi() {
        return revenuNetEmploi;
    }

    /**
     * @return the revenuNetEpouse
     */
    public String getRevenuNetEpouse() {
        return revenuNetEpouse;
    }

    /**
     * @return the revenuTaux
     */
    public String getRevenuTaux() {
        return revenuTaux;
    }

    public String getTotalRevenusNets() {
        return totalRevenusNets;
    }

    /**
     * @param allocationFamiliale
     *            the allocationFamiliale to set
     */
    public void setAllocationFamiliale(String allocationFamiliale) {
        this.allocationFamiliale = allocationFamiliale;
    }

    /**
     * @param deducAppEtu
     *            the deducAppEtu to set
     */
    public void setDeducAppEtu(String deducAppEtu) {
        this.deducAppEtu = deducAppEtu;
    }

    /**
     * @param excDepSuccNp
     *            the excDepSuccNp to set
     */
    public void setExcDepSuccNp(String excDepSuccNp) {
        this.excDepSuccNp = excDepSuccNp;
    }

    /**
     * @param excedDepPropImmoComm
     *            the excedDepPropImmoComm to set
     */
    public void setExcedDepPropImmoComm(String excedDepPropImmoComm) {
        this.excedDepPropImmoComm = excedDepPropImmoComm;
    }

    /**
     * @param excedDepPropImmoPriv
     *            the excedDepPropImmoPriv to set
     */
    public void setExcedDepPropImmoPriv(String excedDepPropImmoPriv) {
        this.excedDepPropImmoPriv = excedDepPropImmoPriv;
    }

    /**
     * @param fortuneImposable
     *            the fortuneImposable to set
     */
    public void setFortuneImposable(String fortuneImposable) {
        this.fortuneImposable = fortuneImposable;
    }

    /**
     * @param fortuneTaux
     *            the fortuneTaux to set
     */
    public void setFortuneTaux(String fortuneTaux) {
        this.fortuneTaux = fortuneTaux;
    }

    @Override
    public void setId(String id) {
        idRevenuContribuable = id;
    }

    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    public void setIdRevenuContribuable(String idRevenuContribuable) {
        this.idRevenuContribuable = idRevenuContribuable;
    }

    /**
     * @param indemniteImposable
     *            the indemniteImposable to set
     */
    public void setIndemniteImposable(String indemniteImposable) {
        this.indemniteImposable = indemniteImposable;
    }

    public void setInteretsPassifsCalcul(String interetsPassifsCalcul) {
        this.interetsPassifsCalcul = interetsPassifsCalcul;
    }

    /**
     * @param interetsPassifsComm
     *            the interetsPassifsComm to set
     */
    public void setInteretsPassifsComm(String interetsPassifsComm) {
        this.interetsPassifsComm = interetsPassifsComm;
    }

    /**
     * @param interetsPassifsPrive
     *            the interetsPassifsPrive to set
     */
    public void setInteretsPassifsPrive(String interetsPassifsPrive) {
        this.interetsPassifsPrive = interetsPassifsPrive;
    }

    public void setPersChargeEnf(String persChargeEnf) {
        this.persChargeEnf = persChargeEnf;
    }

    public void setPerteActAccInd(String perteActAccInd) {
        this.perteActAccInd = perteActAccInd;
    }

    public void setPerteActAgricole(String perteActAgricole) {
        this.perteActAgricole = perteActAgricole;
    }

    public void setPerteActIndep(String perteActIndep) {
        this.perteActIndep = perteActIndep;
    }

    public void setPerteCommercial(String perteCommercial) {
        this.perteCommercial = perteCommercial;
    }

    public void setPerteExercicesComm(String perteExercicesComm) {
        this.perteExercicesComm = perteExercicesComm;
    }

    public void setPerteLiquidation(String perteLiquidation) {
        this.perteLiquidation = perteLiquidation;
    }

    public void setPerteSociete(String perteSociete) {
        this.perteSociete = perteSociete;
    }

    public void setRendFortImmobComm(String rendFortImmobComm) {
        this.rendFortImmobComm = rendFortImmobComm;
    }

    public void setRendFortImmobPrive(String rendFortImmobPrive) {
        this.rendFortImmobPrive = rendFortImmobPrive;
    }

    public void setRevenuImposable(String revenuImposable) {
        this.revenuImposable = revenuImposable;
    }

    public void setRevenuNetEmploi(String revenuNetEmploi) {
        this.revenuNetEmploi = revenuNetEmploi;
    }

    public void setRevenuNetEpouse(String revenuNetEpouse) {
        this.revenuNetEpouse = revenuNetEpouse;
    }

    public void setRevenuTaux(String revenuTaux) {
        this.revenuTaux = revenuTaux;
    }

    public void setTotalRevenusNets(String totalRevenusNets) {
        this.totalRevenusNets = totalRevenusNets;
    }

    public String getDeductionCouplesMaries() {
        return deductionCouplesMaries;
    }

    public void setDeductionCouplesMaries(String deductionCouplesMaries) {
        this.deductionCouplesMaries = deductionCouplesMaries;
    }
}
