package ch.globaz.pegasus.business.domaine.donneeFinanciere.iJAi;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class IjAi extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final Integer nbJour;

    public IjAi(Montant montant, Integer nbJour, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.nbJour = nbJour;

        this.montant = montant.addJournalierPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Integer getNbJour() {
        return nbJour;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.multiply(nbJour).addAnnuelPeriodicity();
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        throw new RuntimeException("not yet implemented");
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.IJAI;
    }

    @Override
    public String toString() {
        return "IjAi [montant=" + montant + ", nbJour=" + nbJour + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((nbJour == null) ? 0 : nbJour.hashCode());
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
        IjAi other = (IjAi) obj;
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (nbJour == null) {
            if (other.nbJour != null) {
                return false;
            }
        } else if (!nbJour.equals(other.nbJour)) {
            return false;
        }
        return true;
    }

}
