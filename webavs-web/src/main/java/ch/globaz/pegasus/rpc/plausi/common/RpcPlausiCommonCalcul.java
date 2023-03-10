package ch.globaz.pegasus.rpc.plausi.common;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData.Depense;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData.Revenu;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

public abstract class RpcPlausiCommonCalcul implements RpcPlausiMetier<RpcPlausiCommonCalculData> {

    public static Revenu buildRevenu(AnnonceDecision decision, RpcPlausiCommonCalculData plausiData, AnnonceCase data) {
        final Revenu revenu = plausiData.getRevenu();

        revenu.FC22 = decision.getRentalValue();
        revenu.FC23 = decision.getUsufructIncome();

        revenu.E3 = decision.getDisabledAllowance();

        revenu.E5 = decision.getSumContributionsAssuranceMaladie();

        revenu.FC20 = decision.getWealthIncome();
        revenu.FC21 = decision.getPropertyIncome();
        revenu.FC24 = decision.getWealthIncomeConsidered();
        revenu.FC41 = decision.getIncomeConsideredTotal();
        
        revenu.E2 = decision.getSumavsAipension();
        revenu.E4 = decision.getSumRenteIj();
        revenu.E12 = decision.getSumTotalRentes();
        revenu.E13 = decision.getSumAutresRevenus();
        
        if (data.getDecisions().size() > 1 && decision.getCoupleSepare()) {
            for (AnnonceDecision deci : data.getDecisions()) {
                if(!deci.equals(decision)) {
                    plausiData.getRevenu().E2 = plausiData.getRevenu().E2.add(deci.getSumavsAipensionNotChild()).divide(2);
                    plausiData.getRevenu().E4 = plausiData.getRevenu().E4.add(deci.getSumRenteIjNotChild()).divide(2);
                    plausiData.getRevenu().E12 = plausiData.getRevenu().E12.add(deci.getSumTotalRentesNotChild()).divide(2);
                    plausiData.getRevenu().E13 = plausiData.getRevenu().E13.add(deci.getSumAutresRevenusNotChild()).divide(2);
                }
            }
        }
        return revenu;
    }

    public static Depense buildDepense(AnnonceDecision decision, RpcPlausiCommonCalculData plausiData,
            Boolean includePrimeLamal) {
        final Depense depense = plausiData.getDepense();

        depense.FC33 = decision.getVitalNeeds();
        depense.FC32 = decision.getInterestFeesEligible();
        depense.FC19 = decision.getGrossRental();

        depense.E20 = decision.getSumHomeTaxeHomePrisEnCompte();
        depense.E22 = decision.getSumHomeParticipationAuxCoutDesPatients();

        depense.E23 = decision.getSumResidencePatientExpenses();

        depense.E24 = includePrimeLamal ? decision.getSumPrimeLamal() : Montant.ZERO_ANNUEL;
        depense.E26 = decision.sumAutresDepenses();

        return depense;
    }
}
