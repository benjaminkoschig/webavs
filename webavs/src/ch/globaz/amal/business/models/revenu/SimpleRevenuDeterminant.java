/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleRevenuDeterminant extends JadeSimpleModel implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String deductionContribAvecEnfantChargeCalcul = null;
    private String deductionContribNonCelibSansEnfantChargeCalcul = null;
    private String deductionSelonNbreEnfantCalcul = null;
    private String excedentDepensesPropImmoCalcul = null;
    private String excedentDepensesSuccNonPartageesCalcul = null;
    private String fortuneImposableCalcul = null;
    private String fortuneImposablePercentCalcul = null;
    private String idContribuable = null;
    private String idRevenuDeterminant = null;
    private String idRevenuHistorique = null;
    private String interetsPassifsCalcul = null;
    private String nbEnfants = null;
    private String partRendementImmobExedantIntPassifsCalcul = null;
    private String perteExercicesCommerciauxCalcul = null;
    private String perteLiquidationCalcul = null;
    private String perteReporteeExercicesCommerciauxCalcul = null;
    private String rendementFortuneImmoCalcul = null;
    private String revenuDeterminantCalcul = null;
    private String revenuImposableCalcul = null;
    private String deductionCouplesMaries = null;

    /**
	 * 
	 */
    public SimpleRevenuDeterminant() {
    }

    @Override
    public SimpleRevenuDeterminant clone() throws CloneNotSupportedException {
        return (SimpleRevenuDeterminant) super.clone();
    }

    /**
     * @return the deductionContribAvecEnfantChargeCalcul
     */
    public String getDeductionContribAvecEnfantChargeCalcul() {
        return deductionContribAvecEnfantChargeCalcul;
    }

    /**
     * @return the deductionContribNonCelibSansEnfantChargeCalcul
     */
    public String getDeductionContribNonCelibSansEnfantChargeCalcul() {
        return deductionContribNonCelibSansEnfantChargeCalcul;
    }

    /**
     * @return the deductionSelonNbreEnfantCalcul
     */
    public String getDeductionSelonNbreEnfantCalcul() {
        return deductionSelonNbreEnfantCalcul;
    }

    /**
     * @return the excedentDepensesPropImmoCalcul
     */
    public String getExcedentDepensesPropImmoCalcul() {
        return excedentDepensesPropImmoCalcul;
    }

    /**
     * @return the excedentDepensesSuccNonPartageesCalcul
     */
    public String getExcedentDepensesSuccNonPartageesCalcul() {
        return excedentDepensesSuccNonPartageesCalcul;
    }

    /**
     * @return the fortuneImposableCalcul
     */
    public String getFortuneImposableCalcul() {
        return fortuneImposableCalcul;
    }

    /**
     * @return the fortuneImposablePercentCalcul
     */
    public String getFortuneImposablePercentCalcul() {
        return fortuneImposablePercentCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdRevenuDeterminant();
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idRevenuDeterminant
     */
    public String getIdRevenuDeterminant() {
        return idRevenuDeterminant;
    }

    /**
     * @return the idRevenuHistorique
     */
    public String getIdRevenuHistorique() {
        return idRevenuHistorique;
    }

    /**
     * @return the interetsPassifsCalcul
     */
    public String getInteretsPassifsCalcul() {
        return interetsPassifsCalcul;
    }

    /**
     * @return the nbEnfants
     */
    public String getNbEnfants() {
        return nbEnfants;
    }

    /**
     * @return the partRendementImmobExedantIntPassifsCalcul
     */
    public String getPartRendementImmobExedantIntPassifsCalcul() {
        return partRendementImmobExedantIntPassifsCalcul;
    }

    /**
     * @return the perteExercicesCommerciauxCalcul
     */
    public String getPerteExercicesCommerciauxCalcul() {
        return perteExercicesCommerciauxCalcul;
    }

    /**
     * @return the perteLiquidationCalcul
     */
    public String getPerteLiquidationCalcul() {
        return perteLiquidationCalcul;
    }

    /**
     * @return the perteReporteeExercicesCommerciauxCalcul
     */
    public String getPerteReporteeExercicesCommerciauxCalcul() {
        return perteReporteeExercicesCommerciauxCalcul;
    }

    /**
     * @return the rendementFortuneImmoCalcul
     */
    public String getRendementFortuneImmoCalcul() {
        return rendementFortuneImmoCalcul;
    }

    /**
     * @return the revenuDeterminantCalcul
     */
    public String getRevenuDeterminantCalcul() {
        return revenuDeterminantCalcul;
    }

    /**
     * @return the revenuImposableCalcul
     */
    public String getRevenuImposableCalcul() {
        return revenuImposableCalcul;
    }

    /**
     * @param deductionContribAvecEnfantChargeCalcul
     *            the deductionContribAvecEnfantChargeCalcul to set
     */
    public void setDeductionContribAvecEnfantChargeCalcul(String deductionContribAvecEnfantChargeCalcul) {
        this.deductionContribAvecEnfantChargeCalcul = deductionContribAvecEnfantChargeCalcul;
    }

    /**
     * @param deductionContribNonCelibSansEnfantChargeCalcul
     *            the deductionContribNonCelibSansEnfantChargeCalcul to set
     */
    public void setDeductionContribNonCelibSansEnfantChargeCalcul(String deductionContribNonCelibSansEnfantChargeCalcul) {
        this.deductionContribNonCelibSansEnfantChargeCalcul = deductionContribNonCelibSansEnfantChargeCalcul;
    }

    /**
     * @param deductionSelonNbreEnfantCalcul
     *            the deductionSelonNbreEnfantCalcul to set
     */
    public void setDeductionSelonNbreEnfantCalcul(String deductionSelonNbreEnfantCalcul) {
        this.deductionSelonNbreEnfantCalcul = deductionSelonNbreEnfantCalcul;
    }

    /**
     * @param excedentDepensesPropImmoCalcul
     *            the excedentDepensesPropImmoCalcul to set
     */
    public void setExcedentDepensesPropImmoCalcul(String excedentDepensesPropImmoCalcul) {
        this.excedentDepensesPropImmoCalcul = excedentDepensesPropImmoCalcul;
    }

    /**
     * @param excedentDepensesSuccNonPartageesCalcul
     *            the excedentDepensesSuccNonPartageesCalcul to set
     */
    public void setExcedentDepensesSuccNonPartageesCalcul(String excedentDepensesSuccNonPartageesCalcul) {
        this.excedentDepensesSuccNonPartageesCalcul = excedentDepensesSuccNonPartageesCalcul;
    }

    /**
     * @param fortuneImposableCalcul
     *            the fortuneImposableCalcul to set
     */
    public void setFortuneImposableCalcul(String fortuneImposableCalcul) {
        this.fortuneImposableCalcul = fortuneImposableCalcul;
    }

    /**
     * @param fortuneImposablePercentCalcul
     *            the fortuneImposablePercentCalcul to set
     */
    public void setFortuneImposablePercentCalcul(String fortuneImposablePercentCalcul) {
        this.fortuneImposablePercentCalcul = fortuneImposablePercentCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idRevenuDeterminant = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idRevenuDeterminant
     *            the idRevenuDeterminant to set
     */
    public void setIdRevenuDeterminant(String idRevenuDeterminant) {
        this.idRevenuDeterminant = idRevenuDeterminant;
    }

    /**
     * @param idRevenuHistorique
     *            the idRevenuHistorique to set
     */
    public void setIdRevenuHistorique(String idRevenuHistorique) {
        this.idRevenuHistorique = idRevenuHistorique;
    }

    /**
     * @param interetsPassifsCalcul
     *            the interetsPassifsCalcul to set
     */
    public void setInteretsPassifsCalcul(String interetsPassifsCalcul) {
        this.interetsPassifsCalcul = interetsPassifsCalcul;
    }

    /**
     * @param nbEnfants
     *            the nbEnfants to set
     */
    public void setNbEnfants(String nbEnfants) {
        this.nbEnfants = nbEnfants;
    }

    /**
     * @param partRendementImmobExedantIntPassifsCalcul
     *            the partRendementImmobExedantIntPassifsCalcul to set
     */
    public void setPartRendementImmobExedantIntPassifsCalcul(String partRendementImmobExedantIntPassifsCalcul) {
        this.partRendementImmobExedantIntPassifsCalcul = partRendementImmobExedantIntPassifsCalcul;
    }

    /**
     * @param perteExercicesCommerciauxCalcul
     *            the perteExercicesCommerciauxCalcul to set
     */
    public void setPerteExercicesCommerciauxCalcul(String perteExercicesCommerciauxCalcul) {
        this.perteExercicesCommerciauxCalcul = perteExercicesCommerciauxCalcul;
    }

    /**
     * @param perteLiquidationCalcul
     *            the perteLiquidationCalcul to set
     */
    public void setPerteLiquidationCalcul(String perteLiquidationCalcul) {
        this.perteLiquidationCalcul = perteLiquidationCalcul;
    }

    /**
     * @param perteReporteeExercicesCommerciauxCalcul
     *            the perteReporteeExercicesCommerciauxCalcul to set
     */
    public void setPerteReporteeExercicesCommerciauxCalcul(String perteReporteeExercicesCommerciauxCalcul) {
        this.perteReporteeExercicesCommerciauxCalcul = perteReporteeExercicesCommerciauxCalcul;
    }

    /**
     * @param rendementFortuneImmoCalcul
     *            the rendementFortuneImmoCalcul to set
     */
    public void setRendementFortuneImmoCalcul(String rendementFortuneImmoCalcul) {
        this.rendementFortuneImmoCalcul = rendementFortuneImmoCalcul;
    }

    /**
     * @param revenuDeterminantCalcul
     *            the revenuDeterminantCalcul to set
     */
    public void setRevenuDeterminantCalcul(String revenuDeterminantCalcul) {
        this.revenuDeterminantCalcul = revenuDeterminantCalcul;
    }

    /**
     * @param revenuImposableCalcul
     *            the revenuImposableCalcul to set
     */
    public void setRevenuImposableCalcul(String revenuImposableCalcul) {
        this.revenuImposableCalcul = revenuImposableCalcul;
    }

    public String getDeductionCouplesMaries() {
        return deductionCouplesMaries;
    }

    public void setDeductionCouplesMaries(String deductionCouplesMaries) {
        this.deductionCouplesMaries = deductionCouplesMaries;
    }

}
