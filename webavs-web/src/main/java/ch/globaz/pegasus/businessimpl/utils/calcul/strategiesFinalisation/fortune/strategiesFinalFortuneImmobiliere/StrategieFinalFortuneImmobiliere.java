package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalFortuneImmobiliere implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        Float deductionForfaitaire = 0f;

        float fortune_bien_immo_principal = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE);
        float fortune_bien_immo_secondaire = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE);
        float fortune_bien_immo_non_habitable = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES);

        float somme = fortune_bien_immo_principal;

        if (fortune_bien_immo_principal > 0) {
            deductionForfaitaire = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE)).getValeurCourante());
            somme -= deductionForfaitaire;
        }
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT,
                UtilStrategieFinalFortuneImmobiliere.plafonneValeurBiensImmoDeduit(somme)));
        if(context.contains(Attribut.REFORME)){
            float fortune_bien_immo_principal_deduit_hypo = UtilStrategieFinalFortuneImmobiliere.plafondDeductionDetteHypo(UtilStrategieFinalFortuneImmobiliere.plafonneValeurBiensImmoDeduit(somme),donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY));
            somme = fortune_bien_immo_principal_deduit_hypo;
            float fortune_bien_immo_secondaire_deduit_hypo = UtilStrategieFinalFortuneImmobiliere.plafondDeductionDetteHypo(fortune_bien_immo_secondaire,donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED));
            somme += fortune_bien_immo_secondaire_deduit_hypo;
            float fortune_bien_immo_non_habitable_deduit_hypo = UtilStrategieFinalFortuneImmobiliere.plafondDeductionDetteHypo(fortune_bien_immo_non_habitable,donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED));
            somme += fortune_bien_immo_non_habitable_deduit_hypo;
        }else{
            somme = UtilStrategieFinalFortuneImmobiliere.plafonneValeurBiensImmoDeduit(somme);
            somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE);
            // somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT);
            somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES);
        }
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL, somme));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE,deductionForfaitaire));
    }
}
