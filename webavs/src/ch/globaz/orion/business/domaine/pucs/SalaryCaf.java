package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;

public class SalaryCaf {
    private Periode periode;
    private Montant montant;
    private String canton;

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    @Override
    public String toString() {
        return "SalaryCaf [periode=" + periode + ", montant=" + montant + ", canton=" + canton + "]";
    }

}
