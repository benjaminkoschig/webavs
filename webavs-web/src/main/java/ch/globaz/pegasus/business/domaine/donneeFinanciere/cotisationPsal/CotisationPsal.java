package ch.globaz.pegasus.business.domaine.donneeFinanciere.cotisationPsal;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class CotisationPsal extends DonneeFinanciere implements Depense {
    private final Montant montant;

    public CotisationPsal(Montant montant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    @Override
    public Montant computeDepense() {
        return montant;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.COTISATION_PSAL;
    }

    @Override
    public String toString() {
        return "CotisationPsal [montant=" + montant + "]";
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
        CotisationPsal other = (CotisationPsal) obj;
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
