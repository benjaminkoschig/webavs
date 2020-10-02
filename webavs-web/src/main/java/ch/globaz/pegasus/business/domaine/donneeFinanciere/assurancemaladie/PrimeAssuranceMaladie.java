package ch.globaz.pegasus.business.domaine.donneeFinanciere.assurancemaladie;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class PrimeAssuranceMaladie extends DonneeFinanciere implements Depense {
    private final Montant montant;

    public PrimeAssuranceMaladie(Montant montant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }


    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.PRIME_ASSURANCE_MALADIE;
    }

    @Override
    public String toString() {
        return "Prime assurance maladie [montant=" + montant + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = ((montant == null) ? 0 : montant.hashCode());
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
        PrimeAssuranceMaladie other = (PrimeAssuranceMaladie) obj;
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        return true;
    }

    @Override
    public Montant computeDepense() {
        return montant.annualise();
    }

}
