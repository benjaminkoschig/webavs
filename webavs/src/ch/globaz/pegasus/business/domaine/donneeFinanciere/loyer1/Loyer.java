package ch.globaz.pegasus.business.domaine.donneeFinanciere.loyer1;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;

public class Loyer extends DonneeFinanciere implements Depense {
    private final Montant montant;
    private final Montant charge;
    private final Montant sousLocation;
    private final Montant taxeJournalierePensionNonReconnue;
    private final LoyerType type;
    private final Integer nbPersonnne;
    private final boolean fauteuilRoulant;
    private final boolean tenueMenage;

    public Loyer(Montant montant, Montant charge, Montant sousLocation, Montant taxeJournalierePensionNonReconnue,
            LoyerType type, Integer nbPersonnne, boolean fauteuilRoulant, boolean tenueMenage,
            DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.type = type;

        this.nbPersonnne = nbPersonnne;
        this.fauteuilRoulant = fauteuilRoulant;
        this.tenueMenage = tenueMenage;

        this.montant = montant.addMensuelPeriodicity();
        this.charge = charge.addMensuelPeriodicity();
        this.sousLocation = sousLocation.addMensuelPeriodicity();
        this.taxeJournalierePensionNonReconnue = taxeJournalierePensionNonReconnue.addJournalierPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getCharge() {
        return charge;
    }

    public Montant getSousLocation() {
        return sousLocation;
    }

    public Montant getTaxeJournalierePensionNonReconnue() {
        return taxeJournalierePensionNonReconnue;
    }

    public LoyerType getType() {
        return type;
    }

    public Integer getNbPersonnne() {
        return nbPersonnne;
    }

    public boolean isFauteuilRoulant() {
        return fauteuilRoulant;
    }

    public boolean isTenueMenage() {
        return tenueMenage;
    }

    public Montant computeCharge() {
        Montant charge = Montant.ZERO_ANNUEL;
        if (type.isBrutChargesComprises()) {
            charge = Montant.ZERO_ANNUEL;
        } else if (type.isNetAvecCharge()) {
            charge = this.charge.annualise();
        } else if (type.isNetAvecChargeForfaitaires()) {
            charge = Montant.ZERO_ANNUEL;
        } else if (type.isNetSansCharge()) {
            charge = Montant.ZERO_ANNUEL;
        } else if (type.isPensionsNonRecounnue()) {
            charge = Montant.ZERO_ANNUEL;
        } else if (type.isValeurLocativeChezProprietaire()) {
            charge = Montant.ZERO_ANNUEL;
        } else {
            throw new RuntimeException(
                    "Impossile de détérminier le type de charge pour calcule la dépense, pour le loyer suivant"
                            + toString());
        }

        return charge;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.LOYER;
    }

    @Override
    public String toString() {
        return "Loyer [montant=" + montant + ", charge=" + charge + ", sousLocation=" + sousLocation
                + ", taxeJournalierePensionNonReconnue=" + taxeJournalierePensionNonReconnue + ", type=" + type
                + ", nbPersonnne=" + nbPersonnne + ", fauteuilRoulant=" + fauteuilRoulant + ", tenueMenage="
                + tenueMenage + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((charge == null) ? 0 : charge.hashCode());
        result = prime * result + (fauteuilRoulant ? 1231 : 1237);
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((nbPersonnne == null) ? 0 : nbPersonnne.hashCode());
        result = prime * result + ((sousLocation == null) ? 0 : sousLocation.hashCode());
        result = prime * result
                + ((taxeJournalierePensionNonReconnue == null) ? 0 : taxeJournalierePensionNonReconnue.hashCode());
        result = prime * result + (tenueMenage ? 1231 : 1237);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Loyer other = (Loyer) obj;
        if (charge == null) {
            if (other.charge != null) {
                return false;
            }
        } else if (!charge.equals(other.charge)) {
            return false;
        }
        if (fauteuilRoulant != other.fauteuilRoulant) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (nbPersonnne == null) {
            if (other.nbPersonnne != null) {
                return false;
            }
        } else if (!nbPersonnne.equals(other.nbPersonnne)) {
            return false;
        }
        if (sousLocation == null) {
            if (other.sousLocation != null) {
                return false;
            }
        } else if (!sousLocation.equals(other.sousLocation)) {
            return false;
        }
        if (taxeJournalierePensionNonReconnue == null) {
            if (other.taxeJournalierePensionNonReconnue != null) {
                return false;
            }
        } else if (!taxeJournalierePensionNonReconnue.equals(other.taxeJournalierePensionNonReconnue)) {
            return false;
        }
        if (tenueMenage != other.tenueMenage) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public Montant computeDepense() {
        return montant.annualise();
    }

}
