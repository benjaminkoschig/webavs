package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;

public class SalaryAvs {
    private Periode periode;
    private Montant montantAvs;
    private Montant montantAc1;
    private Montant montantAc2;

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public Montant getMontantAvs() {
        return montantAvs;
    }

    public void setMontantAvs(Montant montantAvs) {
        this.montantAvs = montantAvs;
    }

    public Montant getMontantAc1() {
        return montantAc1;
    }

    public void setMontantAc1(Montant montantAc1) {
        this.montantAc1 = montantAc1;
    }

    public Montant getMontantAc2() {
        return montantAc2;
    }

    public void setMontantAc2(Montant montantAc2) {
        this.montantAc2 = montantAc2;
    }

    @Override
    public String toString() {
        return "SalaryAvs [periode=" + periode + ", montantAvs=" + montantAvs + ", montantAc1=" + montantAc1
                + ", montantAc2=" + montantAc2 + "]";
    }

}
