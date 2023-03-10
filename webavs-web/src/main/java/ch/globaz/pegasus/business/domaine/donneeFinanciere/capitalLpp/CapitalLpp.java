package ch.globaz.pegasus.business.domaine.donneeFinanciere.capitalLpp;

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

public class CapitalLpp extends DonneeFinanciere implements Revenu, Fortune, FortunePartPropriete, Interet {
    private final Montant montant;
    private final Montant frais;
    private final Montant interet;
    private final Part part;
    private final ProprieteType proprieteType;
    private final boolean sansInteret;

    public CapitalLpp(Montant montant, Montant frais, Montant interet, Part part, ProprieteType proprieteType,
            boolean sansInteret, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.part = part;
        this.proprieteType = proprieteType;
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
    public Montant computeInteret(Taux taux) {
        if (!sansInteret && interet.isZero()) {
            return montant.multiply(taux);
        } else {
            return interet;
        }
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return interet;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        if (!proprieteType.isNuProprietaire()) {
            return interet.substract(frais).multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.CAPITAL_LPP;
    }

    @Override
    public String toString() {
        return "CapitalLpp [montant=" + montant + ", frais=" + frais + ", interet=" + interet + ", part=" + part
                + ", proprieteType=" + proprieteType + ", sansInteret=" + sansInteret + ", parent=" + super.toString()
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((frais == null) ? 0 : frais.hashCode());
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
        CapitalLpp other = (CapitalLpp) obj;
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
        if (proprieteType != other.proprieteType) {
            return false;
        }
        if (sansInteret != other.sansInteret) {
            return false;
        }
        return true;
    }

    @Override
    public Montant computeFortunePartPropriete() {
        return montant.multiply(part);
    }

}
