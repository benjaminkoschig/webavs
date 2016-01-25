package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalFortuneNette implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        float somme = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_MOBI_TOTAL);

        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL);
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL, somme));

        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL);
        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL);

        // final float MONTANT_DEDUCTION_CELIBATAIRE = (Float) context.get(Attribut.CS_DEDUCTION_FORTUNE_CELIBATAIRE);
        // final float MONTANT_DEDUCTION_COUPLE = (Float) context.get(Attribut.CS_DEDUCTION_FORTUNE_COUPLE);
        // final float MONTANT_DEDUCTION_ENFANT = (Float) context.get(Attribut.CS_DEDUCTION_FORTUNE_ENFANT);

        final float MONTANT_DEDUCTION_CELIBATAIRE = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.CS_DEDUCTION_FORTUNE_CELIBATAIRE)).getValeurCourante());
        final float MONTANT_DEDUCTION_COUPLE = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.CS_DEDUCTION_FORTUNE_COUPLE)).getValeurCourante());
        final float MONTANT_DEDUCTION_ENFANT = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.CS_DEDUCTION_FORTUNE_ENFANT)).getValeurCourante());

        float deductionLegale = 0;
        if ((Boolean) context.get(Attribut.IS_FRATRIE)) {
            deductionLegale = MONTANT_DEDUCTION_ENFANT;
        } else {
            if (((Integer) context.get(CalculContext.Attribut.NB_PARENTS)) == 2) {
                deductionLegale = MONTANT_DEDUCTION_COUPLE;
            } else {
                deductionLegale = MONTANT_DEDUCTION_CELIBATAIRE;
            }
        }
        deductionLegale += MONTANT_DEDUCTION_ENFANT * ((Integer) context.get(CalculContext.Attribut.NB_ENFANTS));

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_DED_LEGA_TOTAL, deductionLegale));

        somme -= deductionLegale;

        // plafonnement à zero pour eviter une fortune négative
        somme = Math.max(somme, 0);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL, somme));
    }

}
