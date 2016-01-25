package ch.globaz.pegasus.business.domaine.donneeFinanciere.compteBancairePostal;

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

public class CompteBancairePostal extends DonneeFinanciere implements Fortune, FortunePartPropriete, Revenu, Interet {

    private final Montant montant;
    private final Montant frais;
    private final Montant interet;
    private final Part part;
    private final ProprieteType typePropriete;
    private final boolean sansInteret;

    public CompteBancairePostal(Montant montant, Montant frais, Montant interet, Part part,
            ProprieteType typePropriete, boolean sansInteret, DonneeFinanciere df) {
        super(df);
        this.part = part;
        this.typePropriete = typePropriete;
        this.sansInteret = sansInteret;

        this.montant = montant.addAnnuelPeriodicity();
        this.frais = frais.addAnnuelPeriodicity();
        this.interet = interet.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getFrais() {
        return frais;
    }

    public Montant getInteret() {
        return interet;
    }

    public Part getPart() {
        return part;
    }

    public ProprieteType getTypePropriete() {
        return typePropriete;
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
        if (!typePropriete.isNuProprietaire()) {
            return montant.multiply(part);
        }

        return Montant.ZERO_ANNUEL;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.COMPTE_BANCAIRE_POSTAL;
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
    public String toString() {
        return "CompteBancairePostal [montant=" + montant + ", frais=" + frais + ", interet=" + interet + ", part="
                + part + ", typePropriete=" + typePropriete + ", sansInteret=" + sansInteret + ", parent="
                + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((frais == null) ? 0 : frais.hashCode());
        result = prime * result + ((interet == null) ? 0 : interet.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + (sansInteret ? 1231 : 1237);
        result = prime * result + ((typePropriete == null) ? 0 : typePropriete.hashCode());
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
        CompteBancairePostal other = (CompteBancairePostal) obj;
        if (frais == null) {
            if (other.frais != null) {
                return false;
            }
        } else if (!frais.equals(other.frais)) {
            return false;
        }
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
        if (sansInteret != other.sansInteret) {
            return false;
        }
        if (typePropriete != other.typePropriete) {
            return false;
        }
        return true;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return interet.substract(frais).annualise();
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return interet.annualise();
    }

    @Override
    public Montant computeFortunePartPropriete() {
        return montant.multiply(part);
    }

}
