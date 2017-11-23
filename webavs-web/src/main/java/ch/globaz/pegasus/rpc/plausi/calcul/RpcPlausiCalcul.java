package ch.globaz.pegasus.rpc.plausi.calcul;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.pca.PcaSituation;
import ch.globaz.pegasus.rpc.domaine.MembresFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.rpc.domaine.PersonsElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcPcaDecisionCalculElementCalcul;
import ch.globaz.pegasus.rpc.plausi.calcul.RpcPlausiCalculData.Depense;
import ch.globaz.pegasus.rpc.plausi.calcul.RpcPlausiCalculData.Revenu;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiCalcul implements RpcPlausi<RpcPlausiCalculData> {
    /**
     * @deprecated not used in PRD
     */
    @Deprecated
    public RpcPlausiCalculData buildPlausi(RpcPcaDecisionCalculElementCalcul data,
            MembresFamilleWithDonneesFinanciere membresFamilleWithDonneesFinanciere, PcaSituation situation) {
        RpcPlausiCalculData plausiData = new RpcPlausiCalculData(this);

        plausiData.isCoupleSepare = situation.isCoupleSepare();
        plausiData.pcaGenre = data.getPcaDecision().getPca().getGenre();
        plausiData.pca = data.getPcaDecision().getPca().getMontant();
        plausiData.desc = "idPca:" + data.getPcaDecision().getPca().getId();
        plausiData.etat = data.getCalcul().getEtatCalculFederal();
        plausiData.montantCalculateur = data.getCalcul().getTotalCalcul();

        buildRevenu(data.getCalcul(), data.getPersonsElementsCalcul(), plausiData);
        buildDepense(data.getCalcul(), data.getPersonsElementsCalcul(), plausiData);
        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.CALCUL;
    }

    @Override
    public String getID() {
        return "Calcul";
    }

    @Override
    public String getReferance() {
        return "Calcul";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.NONE;
    }

    @Override
    public List<RpcPlausiApplyToDecision> getApplyTo() {
        return new ArrayList<RpcPlausiApplyToDecision>() {
            {
                add(RpcPlausiApplyToDecision.POSITIVE);
                add(RpcPlausiApplyToDecision.REJECT_FULL);
            }
        };
    }

    private static Revenu buildRevenu(RpcCalcul calcul, PersonsElementsCalcul personsElementsCalculs,
            RpcPlausiCalculData plausiData) {

        Revenu revenu = plausiData.getRevenu();
        revenu.FC22 = calcul.getRevenuValeurLocativeAppHabitePrincipale();

        revenu.FC23 = personsElementsCalculs.sumUsufructIncome(); // TODO droit habitation
        if (plausiData.pcaGenre.isHome()) {
            revenu.E3 = personsElementsCalculs.sumRenteApi();
        } else {
            revenu.E3 = Montant.ZERO_ANNUEL;
        }
        revenu.E5 = personsElementsCalculs.sumHomeContributionLca();
        revenu.FC20 = calcul.getRevenusDeLaFortune();
        revenu.FC21 = calcul.getRevenusFortuneImmobiliere();
        revenu.FC24 = calcul.getRevenusDeLaFortunePrisEnCompte();
        revenu.FC41 = calcul.getRevenusTotalAPrendreEnCompte();

        if (plausiData.isCoupleSepare) {
            revenu.E2 = personsElementsCalculs.sumRenteAvsAi().divide(2).arrondiAUnIntierInferior();
            revenu.E4 = personsElementsCalculs.sumRenteIj().divide(2).arrondiAUnIntierInferior();
            revenu.E12 = personsElementsCalculs.sumTotalRentes().divide(2).arrondiAUnIntierInferior();
            revenu.E13 = personsElementsCalculs.sumAutresRevenus().divide(2).arrondiAUnIntierInferior();
        } else {
            revenu.E2 = personsElementsCalculs.sumRenteAvsAi();
            revenu.E4 = personsElementsCalculs.sumRenteIj();
            revenu.E12 = personsElementsCalculs.sumTotalRentes();
            revenu.E13 = personsElementsCalculs.sumAutresRevenus();
        }
        return revenu;
    }

    private static Depense buildDepense(RpcCalcul calcul, PersonsElementsCalcul personsElementsCalculs,
            RpcPlausiCalculData plausiData) {
        Depense depense = plausiData.getDepense();

        depense.FC33 = calcul.getBesoinsVitaux();
        depense.FC32 = calcul.getInteretsHypothequairesFraisMaintenance();
        depense.FC19 = calcul.getLoyers();

        depense.E20 = personsElementsCalculs.sumHomeTaxeHomePrisEnCompte();
        depense.E22 = personsElementsCalculs.sumHomeParticipationAuxCoutDesPatients();
        depense.E23 = personsElementsCalculs.sumHomeDepensesPersonnelles();
        depense.E26 = personsElementsCalculs.sumAutresDepenses();

        return depense;
    }

}
