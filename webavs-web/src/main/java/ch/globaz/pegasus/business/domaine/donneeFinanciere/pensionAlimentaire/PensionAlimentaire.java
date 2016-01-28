package ch.globaz.pegasus.business.domaine.donneeFinanciere.pensionAlimentaire;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Depense;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class PensionAlimentaire extends DonneeFinanciere implements Revenu, Depense {
    private final Montant montant;
    private final Montant montantRenteEnfant;
    private final PensionAlimentaireType type;
    private final PensionAlimentaireLienParente lienParente;
    private boolean deductionRenteEnfant;

    public PensionAlimentaire(Montant montant, Montant montantRenteEnfant, PensionAlimentaireType type,
            PensionAlimentaireLienParente lienParente, boolean deductionRenteEnfant, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.type = type;
        this.lienParente = lienParente;
        this.deductionRenteEnfant = deductionRenteEnfant;

        this.montant = montant.addMensuelPeriodicity();
        this.montantRenteEnfant = montantRenteEnfant.addMensuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getMontantRenteEnfant() {
        return montantRenteEnfant;
    }

    public PensionAlimentaireType getType() {
        return type;
    }

    public PensionAlimentaireLienParente getLienParente() {
        return lienParente;
    }

    public boolean isDeductionRenteEnfant() {
        return deductionRenteEnfant;
    }

    @Override
    public Montant computeDepense() {
        if (type.isVersee()) {
            if (isDeductionRenteEnfant()) {
                return montant.annualise().substract(montantRenteEnfant.annualise());
            }
            return montant.annualise();
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        if (type.isDue()) {
            return montant.annualise();
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        if (type.isDue()) {
            return montant.annualise();
        }
        return Montant.ZERO_ANNUEL;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.PENSION_ALIMENTAIRE;
    }

    @Override
    public String toString() {
        return "PensionAlimentaire [montant=" + montant + ", montantRenteEnfant=" + montantRenteEnfant + ", type="
                + type + ", lienParente=" + lienParente + ", deductionRenteEnfant=" + deductionRenteEnfant + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (deductionRenteEnfant ? 1231 : 1237);
        result = prime * result + ((lienParente == null) ? 0 : lienParente.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((montantRenteEnfant == null) ? 0 : montantRenteEnfant.hashCode());
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
        PensionAlimentaire other = (PensionAlimentaire) obj;
        if (deductionRenteEnfant != other.deductionRenteEnfant) {
            return false;
        }
        if (lienParente != other.lienParente) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (montantRenteEnfant == null) {
            if (other.montantRenteEnfant != null) {
                return false;
            }
        } else if (!montantRenteEnfant.equals(other.montantRenteEnfant)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

}
