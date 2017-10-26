package ch.globaz.pegasus.business.domaine.donneeFinanciere.pretEnversTiers;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.FortunePartPropriete;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Interet;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class PretEnversTiers extends DonneeFinanciere implements Fortune, FortunePartPropriete, Revenu, Interet {
    private Montant montant;
    private Montant interet;
    private Part part;
    private ProprieteType proprieteType;
    private boolean sansInteret;

    public PretEnversTiers(Montant montant, Montant interet, Part part, ProprieteType proprieteType,
            boolean sansInteret, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.part = part;
        this.proprieteType = proprieteType;
        this.sansInteret = sansInteret;

        this.montant = montant.addAnnuelPeriodicity();
        this.interet = interet.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getInteret() {
        return interet;
    }

    public Part getPart() {
        return part;
    }

    public ProprieteType getProprieteType() {
        return proprieteType;
    }

    public boolean isSansInteret() {
        return sansInteret;
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
        typeDonnneeFianciere = DonneeFinanciereType.PRET_ENVERS_TIERS;
    }

    @Override
    public Montant computeInteret(Taux taux) {
        if (!sansInteret && interet.isZero()) {
            return montant.multiply(taux);
        } else {
            return interet;
        }
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return interet.annualise();
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return interet.annualise();
    }

    @Override
    public Montant computeFortunePartPropriete() {
        return montant.multiply(part);
    }

    @Override
    public String toString() {
        return "PretEnversTiers [montant=" + montant + ", interet=" + interet + ", part=" + part + ", proprieteType="
                + proprieteType + ", sansInteret=" + sansInteret + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((interet == null) ? 0 : interet.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
        result = prime * result + (sansInteret ? 1231 : 1237);
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
        PretEnversTiers other = (PretEnversTiers) obj;
        if (interet == null) {
            if (other.interet != null) {
                return false;
            }
        } else if (!interet.equals(other.interet)) {
            return false;
        }
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
        if (sansInteret != other.sansInteret) {
            return false;
        }
        return true;
    }

}
