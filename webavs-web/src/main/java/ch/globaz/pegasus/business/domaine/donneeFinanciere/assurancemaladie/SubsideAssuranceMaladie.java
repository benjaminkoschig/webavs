package ch.globaz.pegasus.business.domaine.donneeFinanciere.assurancemaladie;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class SubsideAssuranceMaladie extends DonneeFinanciere implements Revenu {
    private final Montant montant;

    public SubsideAssuranceMaladie(Montant montant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");

        this.montant = montant.addMensuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant.annualise();
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.annualise();
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.SUBSIDE_ASSURANCE_MALADIE;
    }

    @Override
    public String toString() {
        return "Subside Assurance Maladie [montant=" + montant + "]";
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
        SubsideAssuranceMaladie other = (SubsideAssuranceMaladie) obj;
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
