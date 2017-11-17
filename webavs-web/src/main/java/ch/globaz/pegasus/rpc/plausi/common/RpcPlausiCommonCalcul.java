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

        // TODO : DCLTODO - Mis à zero d'office ?
        revenu.E3 = Montant.ZERO;// FAire une somme du disabledAllowance;

        revenu.E5 = decision.getSumContributionsAssuranceMaladie();

        revenu.FC20 = decision.getWealthIncome();
        revenu.FC21 = decision.getPropertyIncome();
        revenu.FC24 = decision.getWealthIncomeConsidered();
        revenu.FC41 = decision.getIncomeConsideredTotal();
        if (data.getDecisions().size() > 1) {
            plausiData.getRevenu().E2 = Montant.ZERO;
            plausiData.getRevenu().E4 = Montant.ZERO;
            plausiData.getRevenu().E12 = Montant.ZERO;
            plausiData.getRevenu().E13 = Montant.ZERO;
            for (AnnonceDecision deci : data.getDecisions()) {
                plausiData.getRevenu().E2 = plausiData.getRevenu().E2.add(deci.getSumavsAipension().divide(2));
                plausiData.getRevenu().E4 = plausiData.getRevenu().E4.add(deci.getSumRenteIj().divide(2));
                plausiData.getRevenu().E12 = plausiData.getRevenu().E12.add(deci.getSumTotalRentes().divide(2));
                plausiData.getRevenu().E13 = plausiData.getRevenu().E13.add(deci.getSumAutresRevenus().divide(2));
            }
        } else {
            revenu.E2 = decision.getSumavsAipension();
            revenu.E4 = decision.getSumRenteIj();
            revenu.E12 = decision.getSumTotalRentes();
            revenu.E13 = decision.getSumAutresRevenus();
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
