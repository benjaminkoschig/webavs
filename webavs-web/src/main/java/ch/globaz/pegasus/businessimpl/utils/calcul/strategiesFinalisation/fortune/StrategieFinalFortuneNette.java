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
        if (!context.contains(Attribut.REFORME)) {
            somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL);
        }

        somme -= donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION, somme - montantHabitationPrincipal(donnee)));

        final float MONTANT_DEDUCTION_CELIBATAIRE;
        final float MONTANT_DEDUCTION_COUPLE;
        if (context.contains(Attribut.REFORME)) {
            MONTANT_DEDUCTION_CELIBATAIRE = Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_REFORME_DEDUCTION_FORTUNE_CELIBATAIRES)).getValeurCourante());
            MONTANT_DEDUCTION_COUPLE = Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_REFORME_DEDUCTION_FORTUNE_COUPLES)).getValeurCourante());
        } else {
            MONTANT_DEDUCTION_CELIBATAIRE = Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_DEDUCTION_FORTUNE_CELIBATAIRE)).getValeurCourante());
            MONTANT_DEDUCTION_COUPLE = Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_DEDUCTION_FORTUNE_COUPLE)).getValeurCourante());
        }
        final float MONTANT_DEDUCTION_ENFANT = Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_DEDUCTION_FORTUNE_ENFANT)).getValeurCourante());

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

        if (context.contains(Attribut.REFORME)) {
            int nbPersonnes = (Integer) context.get(Attribut.NB_PARENTS);
            int nbHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES).intValue();
            boolean isAllHome = (nbHomes >= nbPersonnes);
            if (nbPersonnes > 1) {
                Float fraction;
                String legende;
                if (!isAllHome && nbHomes != 0 && donnee.getValeurEnfant(IPCValeursPlanCalcul.PERSONNE_EN_COURS_ISHOME) != null
                        && hasPersonneHabitationPrincipal(donnee)) {
                    if (donnee.getValeurEnfant(IPCValeursPlanCalcul.PERSONNE_EN_COURS_ISHOME) > 0) {
                        fraction = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_FRACTIONS_FORTUNE_HOME)).getValeurCourante());
                        legende = ((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_FRACTIONS_FORTUNE_HOME)).getLegendeCourante();
                    } else {
                        fraction = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT)).getValeurCourante());
                        legende = ((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT)).getLegendeCourante();
                    }
                    somme = somme * fraction;
                    TupleDonneeRapport tupleImputationFortunePart = new TupleDonneeRapport(
                            IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_PART, 0.0f, legende);
                    donnee.addEnfantTuple(tupleImputationFortunePart);

                }
            }
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL, somme));
    }

    private boolean hasPersonneHabitationPrincipal(TupleDonneeRapport donnee) {
        TupleDonneeRapport tupleHabitatPrincipal = donnee.getEnfants().get(
                IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);
        boolean immoPrincipal = donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE);
        if (tupleHabitatPrincipal != null && immoPrincipal) {
            float nbPersonnesImmoPrincipal = tupleHabitatPrincipal
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE_NBPERSONNES);
            return nbPersonnesImmoPrincipal > 0;
        }
        return false;
    }

    private float montantHabitationPrincipal(TupleDonneeRapport donnee) {
        TupleDonneeRapport tupleHabitatPrincipal = donnee.getEnfants().get(
                IPCValeursPlanCalcul.CLE_INTER_HABITATION_PRINCIPALE);
        boolean immoPrincipal = donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE);
        if (tupleHabitatPrincipal != null && immoPrincipal) {
            float montant = tupleHabitatPrincipal
                    .getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_PRINCIPAL);
                return montant;
        }
        return 0f;
    }

}
