package ch.globaz.pegasus.business.domaine.donneeFinanciere.revenueHypothtique;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class RevenuHypothtique extends DonneeFinanciere implements Revenu {
    private final Montant revenuNet;
    private final Montant revenuBrut;
    private final Montant fraisGarde;
    private final Montant deductionSocial;
    private final Montant deductionLpp;
    private final RevenuHypothtiqueMotif motif;

    public RevenuHypothtique(Montant revenuNet, Montant revenuBrut, Montant fraisGarde, Montant deductionSocial,
            Montant deductionLpp, RevenuHypothtiqueMotif motif, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);
        this.motif = motif;

        this.revenuNet = revenuNet.addAnnuelPeriodicity();
        this.revenuBrut = revenuBrut.addAnnuelPeriodicity();
        this.fraisGarde = fraisGarde.addAnnuelPeriodicity();
        this.deductionSocial = deductionSocial.addAnnuelPeriodicity();
        this.deductionLpp = deductionLpp.addAnnuelPeriodicity();
    }

    public Montant getRevenuNet() {
        return revenuNet;
    }

    public Montant getRevenuBrut() {
        return revenuBrut;
    }

    public Montant getFraisGarde() {
        return fraisGarde;
    }

    public Montant getDeductionSocial() {
        return deductionSocial;
    }

    public Montant getDeductionLpp() {
        return deductionLpp;
    }

    public RevenuHypothtiqueMotif getMotif() {
        return motif;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        if (!revenuNet.isZero()) {
            return revenuNet;
        }
        return revenuBrut;
    }

    @Override
    public Montant computeRevenuAnnuel() {
        if (!revenuNet.isZero()) {
            return revenuNet;
        }
        return revenuBrut.substract(deductionLpp).substract(deductionSocial).substract(fraisGarde);
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.REVENU_HYPOTHETIQUE;
    }

    @Override
    public String toString() {
        return "RevenueHypothtique [revenuNet=" + revenuNet + ", revenuBrut=" + revenuBrut + ", fraisGarde="
                + fraisGarde + ", deductionSocial=" + deductionSocial + ", deductionLpp=" + deductionLpp + ", motif="
                + motif + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deductionLpp == null) ? 0 : deductionLpp.hashCode());
        result = prime * result + ((deductionSocial == null) ? 0 : deductionSocial.hashCode());
        result = prime * result + ((fraisGarde == null) ? 0 : fraisGarde.hashCode());
        result = prime * result + ((motif == null) ? 0 : motif.hashCode());
        result = prime * result + ((revenuBrut == null) ? 0 : revenuBrut.hashCode());
        result = prime * result + ((revenuNet == null) ? 0 : revenuNet.hashCode());
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
        RevenuHypothtique other = (RevenuHypothtique) obj;
        if (deductionLpp == null) {
            if (other.deductionLpp != null) {
                return false;
            }
        } else if (!deductionLpp.equals(other.deductionLpp)) {
            return false;
        }
        if (deductionSocial == null) {
            if (other.deductionSocial != null) {
                return false;
            }
        } else if (!deductionSocial.equals(other.deductionSocial)) {
            return false;
        }
        if (fraisGarde == null) {
            if (other.fraisGarde != null) {
                return false;
            }
        } else if (!fraisGarde.equals(other.fraisGarde)) {
            return false;
        }
        if (motif != other.motif) {
            return false;
        }
        if (revenuBrut == null) {
            if (other.revenuBrut != null) {
                return false;
            }
        } else if (!revenuBrut.equals(other.revenuBrut)) {
            return false;
        }
        if (revenuNet == null) {
            if (other.revenuNet != null) {
                return false;
            }
        } else if (!revenuNet.equals(other.revenuNet)) {
            return false;
        }
        return true;
    }
}
