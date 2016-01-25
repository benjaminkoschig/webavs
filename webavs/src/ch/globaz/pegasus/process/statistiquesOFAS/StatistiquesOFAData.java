package ch.globaz.pegasus.process.statistiquesOFAS;

import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee.TypeSeparationCC;

public class StatistiquesOFAData {
    private StatistiquesOFASBeneficiaire beneficiaire = null;
    private StatistiquesOFASDepense depense = null;
    private StatistiquesOFASIFortuneDettes fortuneDettes = null;
    private String idPlanCalcule = null;
    private float montantPc = 0;
    private StatistiquesOFASRevenu revenu = null;
    private TypeSeparationCC statutCalcul = null;
    private boolean isSansPlanDeCalcul = false;

    public StatistiquesOFAData() {
        depense = new StatistiquesOFASDepense();
        beneficiaire = new StatistiquesOFASBeneficiaire();
        fortuneDettes = new StatistiquesOFASIFortuneDettes();
        revenu = new StatistiquesOFASRevenu();
    }

    public TypeSeparationCC getStatutCalcul() {
        return statutCalcul;
    }

    public void setStatutCalcul(TypeSeparationCC statutCalcul) {
        this.statutCalcul = statutCalcul;
    }

    public StatistiquesOFASBeneficiaire getBeneficiaire() {
        return beneficiaire;
    }

    public StatistiquesOFASDepense getDepense() {
        return depense;
    }

    public StatistiquesOFASIFortuneDettes getFortuneDettes() {
        return fortuneDettes;
    }

    public String getIdPlanCalcule() {
        return idPlanCalcule;
    }

    public float getMontantPc() {
        return montantPc;
    }

    public StatistiquesOFASRevenu getRevenu() {
        return revenu;
    }

    public void setBeneficiaire(StatistiquesOFASBeneficiaire beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public void setDepense(StatistiquesOFASDepense depense) {
        this.depense = depense;
    }

    public void setFortuneDettes(StatistiquesOFASIFortuneDettes fortuneDettes) {
        this.fortuneDettes = fortuneDettes;
    }

    public void setIdPlanCalcule(String idPlanCalcule) {
        this.idPlanCalcule = idPlanCalcule;
    }

    public void setMontantPc(float montantPc) {
        this.montantPc = montantPc;
    }

    public void setRevenu(StatistiquesOFASRevenu revenu) {
        this.revenu = revenu;
    }

    public boolean isSansPlanDeCalcul() {
        return isSansPlanDeCalcul;
    }

    public void setSansPlanDeCalcul(boolean isSansPlanDeCalcul) {
        this.isSansPlanDeCalcul = isSansPlanDeCalcul;
    }

}
