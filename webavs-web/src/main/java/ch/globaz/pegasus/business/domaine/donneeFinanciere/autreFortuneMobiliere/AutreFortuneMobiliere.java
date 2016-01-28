package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreFortuneMobiliere;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.FortunePartPropriete;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.ProprieteType;

public class AutreFortuneMobiliere extends DonneeFinanciere implements Fortune, FortunePartPropriete {
    private final Montant montant;
    private final ProprieteType proprieteType;
    private final Part part;
    private final AutreFortuneMobiliereTypeDeFortune typeDeFortune;

    public AutreFortuneMobiliere(Montant montant, ProprieteType proprieteType, Part part,
            AutreFortuneMobiliereTypeDeFortune typeDeFortune, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(proprieteType, "proprieteType");
        Checkers.checkNotNull(part, "part");
        Checkers.checkNotNull(typeDeFortune, "typeDeFortune");

        this.proprieteType = proprieteType;
        this.part = part;
        this.typeDeFortune = typeDeFortune;

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

    public AutreFortuneMobiliereTypeDeFortune getTypeDeFortune() {
        return typeDeFortune;
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
        typeDonnneeFianciere = DonneeFinanciereType.AUTRE_FORTUNE_MOBILIERE;
    }

    @Override
    public String toString() {
        return "AutreFortuneMobiliere [montant=" + montant + ", proprieteType=" + proprieteType + ", part=" + part
                + ", typeDeFortune=" + typeDeFortune + ", toString()=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
        result = prime * result + ((typeDeFortune == null) ? 0 : typeDeFortune.hashCode());
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
        AutreFortuneMobiliere other = (AutreFortuneMobiliere) obj;
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
        if (typeDeFortune != other.typeDeFortune) {
            return false;
        }
        return true;
    }

    @Override
    public Montant computeFortunePartPropriete() {
        return montant.multiply(part);
    }

}
