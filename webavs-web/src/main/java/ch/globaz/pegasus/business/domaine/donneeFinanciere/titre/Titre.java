package ch.globaz.pegasus.business.domaine.donneeFinanciere.titre;

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

public class Titre extends DonneeFinanciere implements Fortune, FortunePartPropriete, Revenu, Interet {

    private Montant montant;
    private Montant droitGarde;
    private Montant rendement;
    private ProprieteType proprieteType;
    private Part part;
    private boolean sansRendement;

    public Titre(Montant montant, Montant droitGarde, Montant rendement, ProprieteType proprieteType, Part part,
            boolean sansRendement, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.proprieteType = proprieteType;
        this.part = part;
        this.sansRendement = sansRendement;

        this.montant = montant.addAnnuelPeriodicity();
        this.droitGarde = droitGarde.addAnnuelPeriodicity();
        this.rendement = rendement.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getDroitGarde() {
        return droitGarde;
    }

    public Montant getRendement() {
        return rendement;
    }

    public ProprieteType getProprieteType() {
        return proprieteType;
    }

    public Part getPart() {
        return part;
    }

    public boolean issansRendement() {
        return sansRendement;
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
        typeDonnneeFianciere = DonneeFinanciereType.TITRE;
    }

    @Override
    public Montant computeInteret(Taux taux) {
        if (!sansRendement && rendement.isZero()) {
            return montant.multiply(taux);
        } else {
            return rendement;
        }
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return rendement.annualise();
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return rendement.annualise();
    }

    @Override
    public Montant computeFortunePartPropriete() {
        return montant.multiply(part);
    }

    @Override
    public String toString() {
        return "Titre [montant=" + montant + ", droitGarde=" + droitGarde + ", rendement=" + rendement
                + ", proprieteType=" + proprieteType + ", part=" + part + ", sansRendement=" + sansRendement
                + ", partent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((droitGarde == null) ? 0 : droitGarde.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((proprieteType == null) ? 0 : proprieteType.hashCode());
        result = prime * result + ((rendement == null) ? 0 : rendement.hashCode());
        result = prime * result + (sansRendement ? 1231 : 1237);
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
        Titre other = (Titre) obj;
        if (droitGarde == null) {
            if (other.droitGarde != null) {
                return false;
            }
        } else if (!droitGarde.equals(other.droitGarde)) {
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
        if (rendement == null) {
            if (other.rendement != null) {
                return false;
            }
        } else if (!rendement.equals(other.rendement)) {
            return false;
        }
        if (sansRendement != other.sansRendement) {
            return false;
        }
        return true;
    }

}
