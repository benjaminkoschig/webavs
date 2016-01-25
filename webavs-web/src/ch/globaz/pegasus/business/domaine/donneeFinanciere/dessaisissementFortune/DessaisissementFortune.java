package ch.globaz.pegasus.business.domaine.donneeFinanciere.dessaisissementFortune;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Interet;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class DessaisissementFortune extends DonneeFinanciere implements Fortune, Revenu, Interet {
    private final Montant montant;
    private final Montant deduction;

    public DessaisissementFortune(Montant montant, Montant deduction, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.montant = montant.addAnnuelPeriodicity();
        this.deduction = deduction.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getDeduction() {
        return deduction;
    }

    @Override
    public Montant computeFortuneBrut() {
        return montant;
    }

    @Override
    public Montant computeFortune() {
        return montant.substract(deduction);
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.DESSAISISSEMENT_FORTUNE;
    }

    @Override
    public Montant computeInteret(Taux taux) {
        return computeFortune().multiply(taux);
    }

    @Override
    public String toString() {
        return "DessaississementFortune [montant=" + montant + ", deduction=" + deduction + "]";
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
        DessaisissementFortune other = (DessaisissementFortune) obj;
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

    // TODO prendre en compte la variable métier intérêt forfaitaire fictif
    @Override
    public Montant computeRevenuAnnuel() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant;
    }

}
