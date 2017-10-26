package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcCalcul;

public class AnnonceResidenceCosts {

    private static final Montant NOT_MAPPED_UNKNOWN_DATA = null;

    protected Montant residenceCostsLodging;
    protected Montant residenceCostsCare;
    protected Montant residenceCostsAssistance;
    protected Montant residenceCostsPatientContribution;
    protected Montant residenceCostsTotal;
    protected Montant residenceCostsConsidered;
    protected Montant residencePatientContribution;
    protected Montant residencePatientExpenses;

    public AnnonceResidenceCosts(PersonElementsCalcul personData, RpcCalcul calcul) {
        if (residenceCostsLodging()) {
            residenceCostsLodging = NOT_MAPPED_UNKNOWN_DATA;
        }
        if (residenceCostsCare()) {
            residenceCostsCare = NOT_MAPPED_UNKNOWN_DATA;
        }
        if (residenceCostsAssistance()) {
            residenceCostsAssistance = NOT_MAPPED_UNKNOWN_DATA;
        }
        if (residenceCostsPatientContribution()) {
            residenceCostsPatientContribution = NOT_MAPPED_UNKNOWN_DATA;
        }
        residenceCostsTotal = personData.getHomeTaxeHomeTotal();
        residenceCostsConsidered = personData.getHomeTaxeHomePrisEnCompte();
        residencePatientContribution = personData.getHomeParticipationAuxCoutDesPatients();
        residencePatientExpenses = calcul.getDepensesPersonnelles();
    }

    private boolean residenceCostsPatientContribution() {
        return false;
    }

    private boolean residenceCostsAssistance() {
        return false;
    }

    private boolean residenceCostsCare() {
        return false;
    }

    private boolean residenceCostsLodging() {
        return false;
    }

    public Montant getResidenceCostsLodging() {
        return residenceCostsLodging;
    }

    public Montant getResidenceCostsCare() {
        return residenceCostsCare;
    }

    public Montant getResidenceCostsAssistance() {
        return residenceCostsAssistance;
    }

    public Montant getResidenceCostsPatientContribution() {
        return residenceCostsPatientContribution;
    }

    public Montant getResidenceCostsTotal() {
        return residenceCostsTotal;
    }

    public Montant getResidenceCostsConsidered() {
        return residenceCostsConsidered;
    }

    public Montant getResidencePatientContribution() {
        return residencePatientContribution;
    }

    public Montant getResidencePatientExpenses() {
        return residencePatientExpenses;
    }

}
