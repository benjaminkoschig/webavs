package globaz.corvus.annonce.domain;

import globaz.globall.util.JADate;

/**
 * REAAL3A
 * Représente le niveau 3A des annonces de rentes
 * 
 * @author lga
 * 
 */
public abstract class REAnnonce3A extends REAnnonce2A {

    private Boolean codeRevenuSplitte;
    private JADate dateDebutAnticipation;
    private Boolean isSurvivant;
    private Integer nbreAnneeAnticipation;
    private Integer nbreAnneeBonifTrans_nombreAnnee;
    private Boolean nbreAnneeBonifTrans_demiAnnee;
    private Integer bta_valeurEntiere;
    private Integer bta_valeurDecimal;
    private Integer reductionAnticipation;

    /**
     * @return the codeRevenuSplitte
     */
    public final Boolean getCodeRevenuSplitte() {
        return codeRevenuSplitte;
    }

    /**
     * @param codeRevenuSplitte the codeRevenuSplitte to set
     */
    public final void setCodeRevenuSplitte(Boolean codeRevenuSplitte) {
        this.codeRevenuSplitte = codeRevenuSplitte;
    }

    /**
     * @return the dateDebutAnticipation
     */
    public final JADate getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    /**
     * @param dateDebutAnticipation the dateDebutAnticipation to set
     */
    public final void setDateDebutAnticipation(JADate dateDebutAnticipation) {
        this.dateDebutAnticipation = dateDebutAnticipation;
    }

    /**
     * @return the isSurvivant
     */
    public final Boolean isSurvivant() {
        return isSurvivant;
    }

    /**
     * @param isSurvivant the isSurvivant to set
     */
    public final void setSurvivant(Boolean isSurvivant) {
        this.isSurvivant = isSurvivant;
    }

    /**
     * @return the nbreAnneeAnticipation
     */
    public final Integer getNbreAnneeAnticipation() {
        return nbreAnneeAnticipation;
    }

    /**
     * @param nbreAnneeAnticipation the nbreAnneeAnticipation to set
     */
    public final void setNbreAnneeAnticipation(Integer nbreAnneeAnticipation) {
        this.nbreAnneeAnticipation = nbreAnneeAnticipation;
    }

    /**
     * @return Le nom d'année de bonification transitoire. Il peut y avoir une demi annnée
     */
    public final Integer getNbreAnneeBonifTrans_nombreAnnee() {
        return nbreAnneeBonifTrans_nombreAnnee;
    }

    /**
     * @return Si une demi-année de bonification transitoire est à prendre en compte.
     */
    public final Boolean getNbreAnneeBonifTrans_isDemiAnnee() {
        return nbreAnneeBonifTrans_demiAnnee;
    }

    /**
     * @param nbreAnneeBonifTrans the nbreAnneeBonifTrans to set
     */
    public final void setNbreAnneeBonifTrans(Integer nbreAnneeBonifTrans, Boolean isDemiAnnee) {
        nbreAnneeBonifTrans_nombreAnnee = nbreAnneeBonifTrans;
        nbreAnneeBonifTrans_demiAnnee = isDemiAnnee;
    }

    /**
     * Retourne la valeur entière du nombre d'année de bonification pour tâche éducative
     * 
     * @return la valeur entière du nombre d'année de bonification pour tâche éducative
     */
    public final Integer getBTA_valeurEntiere() {
        return bta_valeurEntiere;
    }

    /**
     * Retourne la fraction d'année de BTA
     * 
     * @return la fraction d'année (la valeur décimale) de BTA
     */
    public final Integer getBTA_valeurDecimal() {
        return bta_valeurDecimal;
    }

    /**
     * Définit le nombre d'année de bonification pour tâche éducative
     * 
     * @param nbreAnneeBTA le nombre d'année de bonification pour tâche éducative
     * @param isDemiBTA <code>true</code> si une demi-année de BTA est à considérer (ex : 10.5 => [10, true])
     */
    public final void setNbreAnneeBTA(Integer valeurEntiere, Integer valeurDecimal) {
        bta_valeurEntiere = valeurEntiere;
        bta_valeurDecimal = valeurDecimal;
    }

    // /**
    // * Retourne <code>true</code> si une demi-année de BTA est à considérer (ex : 10.5 => [10, true])
    // *
    // * @return <code>true</code> si une demi-année de BTA est à considérer (ex : 10.5 => [10, true])
    // */
    // public final Boolean isDemiBTA() {
    // return isDemiBTA;
    // }

    /**
     * @return the reductionAnticipation
     */
    public final Integer getReductionAnticipation() {
        return reductionAnticipation;
    }

    /**
     * @param reductionAnticipation the reductionAnticipation to set
     */
    public final void setReductionAnticipation(Integer reductionAnticipation) {
        this.reductionAnticipation = reductionAnticipation;
    }

}
