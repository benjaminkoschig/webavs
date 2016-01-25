package ch.globaz.pegasus.business.domaine.donneeFinanciere.indeminteJournaliereApg;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciere;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Revenu;

public class IndemniteJournaliereApg extends DonneeFinanciere implements Revenu {
    private final Montant montant;
    private final Montant montantChomage;
    private final Montant cotisationLpp;
    private final Montant gainIntermediaireAnnuel;
    private final IndemniteJournaliereApgGenre genre;
    private final Integer nbJour;
    private final Taux tauxAa;
    private final Taux tauxAvs;
    private final String libelleAutre;

    public IndemniteJournaliereApg(Montant montant, Montant montantChomage, Montant cotisationLpp,
            Montant gainIntermediaireAnnuel, IndemniteJournaliereApgGenre genre, Integer nbJour, Taux tauxAa,
            Taux tauxAvs, String libelleAutre, DonneeFinanciere donneeFinanciere) {
        super(donneeFinanciere);

        this.nbJour = nbJour;
        this.genre = genre;
        this.tauxAa = tauxAa;
        this.tauxAvs = tauxAvs;
        this.libelleAutre = libelleAutre;

        this.montant = montant.addJournalierPeriodicity();
        this.montantChomage = montantChomage.addJournalierPeriodicity();
        this.cotisationLpp = cotisationLpp.addMensuelPeriodicity();
        this.gainIntermediaireAnnuel = gainIntermediaireAnnuel.addAnnuelPeriodicity();

    }

    public Montant getMontant() {
        return montant;
    }

    public Montant getMontantChomage() {
        return montantChomage;
    }

    public Montant getCotisationLpp() {
        return cotisationLpp;
    }

    public Montant getGainIntermediaireAnnuel() {
        return gainIntermediaireAnnuel;
    }

    public IndemniteJournaliereApgGenre getGenre() {
        return genre;
    }

    public Integer getNbJour() {
        return nbJour;
    }

    public Taux getTauxAa() {
        return tauxAa;
    }

    public Taux getTauxAvs() {
        return tauxAvs;
    }

    public String getLibelleAutre() {
        return libelleAutre;
    }

    @Override
    public Montant computeRevenuAnnuelBrut() {
        throw new RuntimeException("not yet implemented");
    }

    @Override
    public Montant computeRevenuAnnuel() {
        Montant montantAnnuelIjNEt = Montant.ZERO_ANNUEL;
        if (genre.isIjChomage()) {
            Montant iJannulalise = montantChomage.annualise();
            Montant deductionAvs = iJannulalise.multiply(tauxAvs);
            Montant deductionAa = iJannulalise.multiply(tauxAa);
            Montant montantLPP = cotisationLpp.divide(nbJour).annualise();
            Montant deductionsTotal = deductionAvs.add(deductionAa).add(montantLPP);
            montantAnnuelIjNEt = iJannulalise.substract(deductionsTotal).substract(gainIntermediaireAnnuel);
        } else {
            montantAnnuelIjNEt = montant.multiply(nbJour).addAnnuelPeriodicity();
        }
        return montantAnnuelIjNEt;
    }

    @Override
    protected void definedTypeDonneeFinanciere() {
        typeDonnneeFianciere = DonneeFinanciereType.INDEMNITE_JOURNLIERE_APG;
    }

    @Override
    public String toString() {
        return "IndeminteJournaliereApg [montant=" + montant + ", montantChomage=" + montantChomage
                + ", cotisationLpp=" + cotisationLpp + ", gainIntermediaireAnnuel=" + gainIntermediaireAnnuel
                + ", genre=" + genre + ", nbJour=" + nbJour + ", tauxAa=" + tauxAa + ", tauxAvs=" + tauxAvs
                + ", libelleAutre=" + libelleAutre + ", parent=" + super.toString() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((cotisationLpp == null) ? 0 : cotisationLpp.hashCode());
        result = prime * result + ((gainIntermediaireAnnuel == null) ? 0 : gainIntermediaireAnnuel.hashCode());
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((libelleAutre == null) ? 0 : libelleAutre.hashCode());
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((montantChomage == null) ? 0 : montantChomage.hashCode());
        result = prime * result + ((nbJour == null) ? 0 : nbJour.hashCode());
        result = prime * result + ((tauxAa == null) ? 0 : tauxAa.hashCode());
        result = prime * result + ((tauxAvs == null) ? 0 : tauxAvs.hashCode());
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
        IndemniteJournaliereApg other = (IndemniteJournaliereApg) obj;
        if (cotisationLpp == null) {
            if (other.cotisationLpp != null) {
                return false;
            }
        } else if (!cotisationLpp.equals(other.cotisationLpp)) {
            return false;
        }
        if (gainIntermediaireAnnuel == null) {
            if (other.gainIntermediaireAnnuel != null) {
                return false;
            }
        } else if (!gainIntermediaireAnnuel.equals(other.gainIntermediaireAnnuel)) {
            return false;
        }
        if (genre != other.genre) {
            return false;
        }
        if (libelleAutre == null) {
            if (other.libelleAutre != null) {
                return false;
            }
        } else if (!libelleAutre.equals(other.libelleAutre)) {
            return false;
        }
        if (montant == null) {
            if (other.montant != null) {
                return false;
            }
        } else if (!montant.equals(other.montant)) {
            return false;
        }
        if (montantChomage == null) {
            if (other.montantChomage != null) {
                return false;
            }
        } else if (!montantChomage.equals(other.montantChomage)) {
            return false;
        }
        if (nbJour == null) {
            if (other.nbJour != null) {
                return false;
            }
        } else if (!nbJour.equals(other.nbJour)) {
            return false;
        }
        if (tauxAa == null) {
            if (other.tauxAa != null) {
                return false;
            }
        } else if (!tauxAa.equals(other.tauxAa)) {
            return false;
        }
        if (tauxAvs == null) {
            if (other.tauxAvs != null) {
                return false;
            }
        } else if (!tauxAvs.equals(other.tauxAvs)) {
            return false;
        }
        return true;
    }

}
