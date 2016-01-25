package ch.globaz.pegasus.business.domaine.donneeFinanciere.vehicule;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.FortunePartPropriete;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class Vehicule extends DonneeFinanciere implements Fortune, FortunePartPropriete {
    private Montant montant;
    private ProprieteType proprieteType;
    private Part part;

    public Vehicule(Montant montant, ProprieteType proprieteType, Part part, DonneeFinanciere df) {
        super(df);

        this.proprieteType = proprieteType;
        this.part = part;

        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public ProprieteType getProprieteType() {
        return proprieteType;
    }

    public Part getPart() {
        return part;
    }

    @Override
    public Montant computeFortuneBrut() {
        return montant;
    }

    @Override
    public Montant computeFortune() {
        if (!proprieteType.isNuProprietaire()) {
            return montant.multiply(part);
        }

        return Montant.ZERO_ANNUEL;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.VEHICULE;
    }

    @Override
    public String toString() {
        return "Vehicule [montant=" + montant + ", proprieteType=" + proprieteType + ", part=" + part + ", parent="
                + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
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
        Vehicule other = (Vehicule) obj;
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (part == null) {
            if (other.part != null) {
                return false;
            }
        } else if (!part.equals(other.part)) {
            return false;
        }
        if (proprieteType != other.proprieteType) {
            return false;
        }
        return true;
    }

    @Override
    public Montant computeFortunePartPropriete() {
        return montant.multiply(part);
    }

}
