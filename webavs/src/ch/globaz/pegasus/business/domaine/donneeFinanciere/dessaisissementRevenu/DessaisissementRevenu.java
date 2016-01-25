package ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementRevenu;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class DessaisissementRevenu extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final Montant deduction;

    public DessaisissementRevenu(Montant montant, Montant deduction, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.montant = montant.addAnnuelPeriodicity();
        this.deduction = deduction.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.substract(deduction);
    }

    public Montant getDeduction() {
        return deduction;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.DESSAISISSEMENT_REVENU;
    }

    @Override
    public String toString() {
        return "DessaisissementRevenu [montant=" + montant + ", deduction=" + deduction + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deduction == null) ? 0 : deduction.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DessaisissementRevenu other = (DessaisissementRevenu) obj;
        if (deduction == null) {
            if (other.deduction != null) {
                return false;
            }
        } else if (!deduction.equals(other.deduction)) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        return true;
    }

}
