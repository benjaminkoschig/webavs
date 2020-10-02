package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueActiviteLucrativeDependante;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class RevenuActiviteLucrativeDependante extends DonneeFinanciere implements Revenu {
    private Montant montant;
    private Montant deductionLpp;
    private Montant deductionSociale;
    private Montant fraisDeGarde;
    private Montant frais;

    public RevenuActiviteLucrativeDependante(Montant montant, Montant deductionLpp, Montant deductionSociale, Montant fraisDeGarde,
            Montant frais, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.montant = montant.addAnnuelPeriodicity();
        this.deductionLpp = deductionLpp.addAnnuelPeriodicity();
        this.deductionSociale = deductionSociale.addAnnuelPeriodicity();
        this.fraisDeGarde = fraisDeGarde.addAnnuelPeriodicity();
        this.frais = frais.addAnnuelPeriodicity();
    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getDeductionLpp() {
        return deductionLpp;
    }

    public Montant getDeductionSociale() {
        return deductionSociale;
    }

    public Montant getFrais() {
        return frais;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        return montant;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        return montant.substract(deductionLpp).substract(deductionSociale).substract(frais);
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE;
    }

    @Override
    public String toString() {
        return "RevenueActiviteLucrativeDependante [montant=" + montant + ", deductionLpp=" + deductionLpp
                + ", deductionSociale=" + deductionSociale + ", fraisDeGarde=" + fraisDeGarde +", frais=" + frais + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deductionLpp == null) ? 0 : deductionLpp.hashCode());
        result = prime * result + ((deductionSociale == null) ? 0 : deductionSociale.hashCode());
        result = prime * result + ((frais == null) ? 0 : frais.hashCode());
        result = prime * result + ((fraisDeGarde == null) ? 0 : fraisDeGarde.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
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
        RevenuActiviteLucrativeDependante other = (RevenuActiviteLucrativeDependante) obj;
        if (deductionLpp == null) {
            if (other.deductionLpp != null) {
                return false;
            }
        } else if (!deductionLpp.equals(other.deductionLpp)) {
            return false;
        }
        if (deductionSociale == null) {
            if (other.deductionSociale != null) {
                return false;
            }
        } else if (!deductionSociale.equals(other.deductionSociale)) {
            return false;
        }
        if (frais == null) {
            if (other.frais != null) {
                return false;
            }
        } else if (!frais.equals(other.frais)) {
            return false;
        }
        if (fraisDeGarde == null) {
            if (other.fraisDeGarde != null) {
                return false;
            }
        } else if (!fraisDeGarde.equals(other.fraisDeGarde)) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        return true;
    }
    public Montant getFraisDeGarde() {
        return fraisDeGarde;
    }

    public void setFraisDeGarde(Montant fraisDeGarde) {
        this.fraisDeGarde = fraisDeGarde;
    }

}
