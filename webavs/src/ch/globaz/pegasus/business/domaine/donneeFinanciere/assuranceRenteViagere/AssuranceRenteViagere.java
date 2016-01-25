package ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Fortune;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class AssuranceRenteViagere extends DonneeFinanciere implements Revenu, Fortune {
    private final Montant montant;
    private final Montant excedant;
    private final Montant valeurDeRachat;

    public AssuranceRenteViagere(Montant montant, Montant excedant, Montant valeurDeRachat,
            DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        Checkers.checkNotNull(montant, "montant");
        Checkers.checkNotNull(excedant, "excedant");
        Checkers.checkNotNull(valeurDeRachat, "valeurDeRachat");

        this.excedant = excedant.addAnnuelPeriodicity();
        this.valeurDeRachat = valeurDeRachat.addAnnuelPeriodicity();
        this.montant = montant.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getExcedant() {
        return excedant;
    }

    public Montant getValeurDeRachat() {
        return valeurDeRachat;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.ASSURANCE_RENTE_VIAGERE;
    }

    @Override
    public Montant computeFortuneBrut() {
        return valeurDeRachat;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.add(excedant).annualise();
    }

    @Override
    public Montant computeFortune() {
        return valeurDeRachat;
    }

    @Override
    public String toString() {
        return "AssuranceRenteViagere [montant=" + montant + ", excedant=" + excedant + ", valeurDeRachat="
                + valeurDeRachat + ", toString()=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((excedant == null) ? 0 : excedant.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((valeurDeRachat == null) ? 0 : valeurDeRachat.hashCode());
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
        AssuranceRenteViagere other = (AssuranceRenteViagere) obj;
        if (excedant == null) {
            if (other.excedant != null) {
                return false;
            }
        } else if (!excedant.equals(other.excedant)) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (valeurDeRachat == null) {
            if (other.valeurDeRachat != null) {
                return false;
            }
        } else if (!valeurDeRachat.equals(other.valeurDeRachat)) {
            return false;
        }
        return true;
    }

}
