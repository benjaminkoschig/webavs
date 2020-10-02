package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

import java.util.Date;

public class StrategieFinalRevenuActiviteLucrativeConjoint implements StrategieCalculFinalisation {

    private final static String[] champsAjoute = {IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_CONJOINT};

    private final static String[] champsASupprimer = {IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_CONJOINT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_CONJOINT};

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        if (context.contains(CalculContext.Attribut.REFORME)) {

            final float TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_NON_RENTIER = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_TAUX_REVENUS_NON_RENTIER)).getValeurCourante());
            final String TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_NON_RENTIER_LEGENDE = ((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_TAUX_REVENUS_NON_RENTIER)).getLegendeCourante();


            final float TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_IJAI = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_TAUX_REVENUS_IJAI)).getValeurCourante());
            final String TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_IJAI_LEGENDE = ((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_TAUX_REVENUS_IJAI)).getLegendeCourante();

            if (donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_IS_FORFAIT_REVENU_NATURE_TENUE_MENAGE) > 0) {
                Float forfaitTenueMenage = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(CalculContext.Attribut.CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE)).getValeurCourante());
                donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_CONJOINT,
                        forfaitTenueMenage
                                + donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_CONJOINT)));
            }

            float somme = 0;
            for (String champ : StrategieFinalRevenuActiviteLucrativeConjoint.champsAjoute) {
                somme += donnee.getValeurEnfant(champ);
            }

            // autres champs à déduire systématiquement
            for (String champ : StrategieFinalRevenuActiviteLucrativeConjoint.champsASupprimer) {
                somme -= donnee.getValeurEnfant(champ);
            }

            if (somme < 0.00f) {
                somme = 0;
            }

            // ajout du revenu de l'activité lucrative arrondi si IJAJ ET pas d'activite lucrative simultanément
            float revenuPrivilegie;
            TupleDonneeRapport tupleActiviteLucrativeRevenuPrivilegie;

            if (context.contains(CalculContext.Attribut.CONJOINT_HAS_IJAI) || context.contains(CalculContext.Attribut.REQUERANT_HAS_IJAI)) {
                revenuPrivilegie = Math.round(somme * TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_IJAI);
                tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_CONJOINT, revenuPrivilegie);
                tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_IJAI_LEGENDE);
            } else {
                revenuPrivilegie = Math.round(somme * TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_NON_RENTIER);
                tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_CONJOINT, revenuPrivilegie);
                tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_CONJOINT_NON_RENTIER_LEGENDE);
            }

            donnee.addEnfantTuple(tupleActiviteLucrativeRevenuPrivilegie);

            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_CONJOINT, somme));

        }
    }

}
