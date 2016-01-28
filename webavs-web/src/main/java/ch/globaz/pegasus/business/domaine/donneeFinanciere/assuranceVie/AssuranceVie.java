package ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceVie;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;

public class AssuranceVie extends DonneeFinanciere implements Fortune {
    private final Montant montant;

    public AssuranceVie(Montant montant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    @Override
    public Montant computeFortuneBrut() {
        return montant;
    }

    @Override
    public Montant computeFortune() {
        return montant;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.ASSURANCE_VIE;
    }

    @Override
    public String toString() {
        return "AssuranceVie [montant=" + montant + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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
        AssuranceVie other = (AssuranceVie) obj;
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
