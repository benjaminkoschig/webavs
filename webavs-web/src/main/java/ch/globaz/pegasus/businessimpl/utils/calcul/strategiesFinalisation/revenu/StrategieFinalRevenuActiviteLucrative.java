package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

import java.util.Date;

public class StrategieFinalRevenuActiviteLucrative implements StrategieCalculFinalisation {
    private final static String[] champsAjoute = {IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE};

    private final static String[] champsASupprimer = {IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU};

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        final float TAUX_REVENU_ACTIVITE_LUCRATIVE = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getValeurCourante());
        final String TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE = ((ControlleurVariablesMetier) context
                .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getLegendeCourante();

        float deductionForfaitaireRevenu = 0f;

        // Dans le cadre de la réforme, on sépare les revenus requérant et conjoint et on doit donc distinguer qui possède une IJAI.
        boolean isIJAI;
        if (context.contains(Attribut.REFORME)) {
            isIJAI = context.contains(Attribut.REQUERANT_HAS_IJAI);
        } else {
            isIJAI = donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI);
        }

        if (donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_IS_FORFAIT_REVENU_NATURE_TENUE_MENAGE) > 0) {
            Float forfaitTenueMenage = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE)).getValeurCourante());
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE,
                    forfaitTenueMenage
                            + donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE)));
        }

        float somme = 0;
        for (String champ : StrategieFinalRevenuActiviteLucrative.champsAjoute) {
            somme += donnee.getValeurEnfant(champ);
        }

        // déduction forfaitaire si IJAJ et activite lucrative simultanément
        if (isIJAI) {
            donnee.addEnfantTuple(new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU, deductionForfaitaireRevenu));
        } else {
            if (somme > 0) {
                int nbPersonnes = (Integer) context.get(Attribut.NB_PERSONNES);
                boolean isFratrie = Boolean.parseBoolean(context.get(Attribut.IS_FRATRIE).toString());
                // Dans le cadre de la réforme, on contrôle si on est dans une fratrie.
                if (context.contains(Attribut.REFORME)) {
                    if (nbPersonnes > 1 && !isFratrie) {
                        deductionForfaitaireRevenu = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE)).getValeurCourante());
                    } else {
                        deductionForfaitaireRevenu = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES)).getValeurCourante());
                    }
                } else {
                    if (nbPersonnes > 1) {
                        deductionForfaitaireRevenu = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE)).getValeurCourante());
                    } else {
                        deductionForfaitaireRevenu = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES)).getValeurCourante());
                    }
                }
                donnee.addEnfantTuple(new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU,
                        deductionForfaitaireRevenu));
            }
        }

        // autres champs à déduire systématiquement
        for (String champ : StrategieFinalRevenuActiviteLucrative.champsASupprimer) {
            somme -= donnee.getValeurEnfant(champ);
        }

        if (somme < 0.00f) {
            somme = 0;
        }

        // ajout du revenu de l'activité lucrative arrondi si IJAJ ET pas d'activite lucrative simultanément
        float revenuPrivilegie = somme;
        TupleDonneeRapport tupleActiviteLucrativeRevenuPrivilegie;

        if (isIJAI) {
            tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE, revenuPrivilegie);
        } else {
            revenuPrivilegie = Math.round(somme * TAUX_REVENU_ACTIVITE_LUCRATIVE);

            tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE, revenuPrivilegie);
            tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE);
        }

        donnee.addEnfantTuple(tupleActiviteLucrativeRevenuPrivilegie);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_NON_PLAFFONNE, somme));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL, somme));

    }

}
