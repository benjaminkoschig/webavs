package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRevenuSourcier extends JadeSimpleModel implements Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String cotisationAc = null;
    private String cotisationAcSupplementaires = null;
    private String cotisationAvsAiApg = null;
    private String deductionAssurances = null;
    private String deductionAssurancesEnfant = null;
    private String deductionAssurancesJeunes = null;
    private String deductionDoubleGain = null;
    private String deductionEnfants = null;
    private String deductionFraisObtention = null;
    private String idRevenu = null;
    private String idRevenuSourcier = null;
    private String nombreMois = null;
    private String primesAANP = null;
    private String primesLPP = null;
    private String revenuEpouseAnnuel = null;
    private String revenuEpouseMensuel = null;
    private String revenuEpouxAnnuel = null;
    private String revenuEpouxMensuel = null;
    private String revenuImposable = null;
    private String revenuPrisEnCompte = null;

    @Override
    public SimpleRevenuSourcier clone() throws CloneNotSupportedException {
        return (SimpleRevenuSourcier) super.clone();
    }

    public String getCotisationAc() {
        return cotisationAc;
    }

    public String getCotisationAcSupplementaires() {
        return cotisationAcSupplementaires;
    }

    public String getCotisationAvsAiApg() {
        return cotisationAvsAiApg;
    }

    public String getDeductionAssurances() {
        return deductionAssurances;
    }

    public String getDeductionAssurancesEnfant() {
        return deductionAssurancesEnfant;
    }

    public String getDeductionAssurancesJeunes() {
        return deductionAssurancesJeunes;
    }

    public String getDeductionDoubleGain() {
        return deductionDoubleGain;
    }

    public String getDeductionEnfants() {
        return deductionEnfants;
    }

    public String getDeductionFraisObtention() {
        return deductionFraisObtention;
    }

    @Override
    public String getId() {
        return idRevenuSourcier;
    }

    public String getIdRevenu() {
        return idRevenu;
    }

    public String getIdRevenuSourcier() {
        return idRevenuSourcier;
    }

    /**
     * @return the nombreMois
     */
    public String getNombreMois() {
        return nombreMois;
    }

    public String getPrimesAANP() {
        return primesAANP;
    }

    public String getPrimesLPP() {
        return primesLPP;
    }

    /**
     * @return the revenuEpouseAnnuel
     */
    public String getRevenuEpouseAnnuel() {
        return revenuEpouseAnnuel;
    }

    /**
     * @return the revenuEpouseMensuel
     */
    public String getRevenuEpouseMensuel() {
        return revenuEpouseMensuel;
    }

    /**
     * @return the revenuEpouxAnnuel
     */
    public String getRevenuEpouxAnnuel() {
        return revenuEpouxAnnuel;
    }

    /**
     * @return the revenuEpouxMensuel
     */
    public String getRevenuEpouxMensuel() {
        return revenuEpouxMensuel;
    }

    /**
     * @return the revenuImposable
     */
    public String getRevenuImposable() {
        return revenuImposable;
    }

    public String getRevenuPrisEnCompte() {
        return revenuPrisEnCompte;
    }

    public void setCotisationAc(String cotisationAc) {
        this.cotisationAc = cotisationAc;
    }

    public void setCotisationAcSupplementaires(String cotisationAcSupplementaires) {
        this.cotisationAcSupplementaires = cotisationAcSupplementaires;
    }

    public void setCotisationAvsAiApg(String cotisationAvsAiApg) {
        this.cotisationAvsAiApg = cotisationAvsAiApg;
    }

    public void setDeductionAssurances(String deductionAssurances) {
        this.deductionAssurances = deductionAssurances;
    }

    public void setDeductionAssurancesEnfant(String deductionAssurancesEnfant) {
        this.deductionAssurancesEnfant = deductionAssurancesEnfant;
    }

    public void setDeductionAssurancesJeunes(String deductionAssurancesJeunes) {
        this.deductionAssurancesJeunes = deductionAssurancesJeunes;
    }

    public void setDeductionDoubleGain(String deductionDoubleGain) {
        this.deductionDoubleGain = deductionDoubleGain;
    }

    public void setDeductionEnfants(String deductionEnfants) {
        this.deductionEnfants = deductionEnfants;
    }

    public void setDeductionFraisObtention(String deductionFraisObtention) {
        this.deductionFraisObtention = deductionFraisObtention;
    }

    @Override
    public void setId(String id) {
        idRevenuSourcier = id;
    }

    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    public void setIdRevenuSourcier(String idRevenuSourcier) {
        this.idRevenuSourcier = idRevenuSourcier;
    }

    public void setNombreMois(String nombreMois) {
        this.nombreMois = nombreMois;
    }

    public void setPrimesAANP(String primesAANP) {
        this.primesAANP = primesAANP;
    }

    public void setPrimesLPP(String primesLPP) {
        this.primesLPP = primesLPP;
    }

    public void setRevenuEpouseAnnuel(String revenuEpouseAnnuel) {
        this.revenuEpouseAnnuel = revenuEpouseAnnuel;
    }

    public void setRevenuEpouseMensuel(String revenuEpouseMensuel) {
        this.revenuEpouseMensuel = revenuEpouseMensuel;
    }

    public void setRevenuEpouxAnnuel(String revenuEpouxAnnuel) {
        this.revenuEpouxAnnuel = revenuEpouxAnnuel;
    }

    public void setRevenuEpouxMensuel(String revenuEpouxMensuel) {
        this.revenuEpouxMensuel = revenuEpouxMensuel;
    }

    /**
     * @param revenuImposable
     *            the revenuImposable to set
     */
    public void setRevenuImposable(String revenuImposable) {
        this.revenuImposable = revenuImposable;
    }

    public void setRevenuPrisEnCompte(String revenuPrisEnCompte) {
        this.revenuPrisEnCompte = revenuPrisEnCompte;
    }

}
