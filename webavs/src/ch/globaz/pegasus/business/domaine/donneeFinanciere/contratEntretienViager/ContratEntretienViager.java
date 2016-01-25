package ch.globaz.pegasus.business.domaine.donneeFinanciere.contratEntretienViager;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class ContratEntretienViager extends DonneeFinanciere implements Revenu {
    private final Montant montant;

    public ContratEntretienViager(Montant montant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.CONTRAT_ENTRETIEN_VIAGER;
    }

    @Override
    public String toString() {
        return "ContratEntretienViager [montant=" + montant + "]";
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
        ContratEntretienViager other = (ContratEntretienViager) obj;
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
    public Montant computeRevenuAnnuel() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant;
    }

}
