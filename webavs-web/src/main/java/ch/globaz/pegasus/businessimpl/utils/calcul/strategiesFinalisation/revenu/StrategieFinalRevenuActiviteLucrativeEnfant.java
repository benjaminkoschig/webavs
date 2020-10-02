package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

import java.util.Date;

public class StrategieFinalRevenuActiviteLucrativeEnfant implements StrategieCalculFinalisation {

    private final static String[] champsAjoute = {IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE_ENFANT};

    private final static String[] champsASupprimer = {IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP_ENFANT,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU_ENFANT};

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        if (context.contains(CalculContext.Attribut.REFORME)) {

            final float TAUX_REVENU_ACTIVITE_LUCRATIVE_ENFANT = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT)).getValeurCourante());
            final String TAUX_REVENU_ACTIVITE_LUCRATIVE_ENFANT_LEGENDE = ((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT)).getLegendeCourante();

            float deductionForfaitaireRevenu = 0f;

            if (donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_IS_FORFAIT_REVENU_NATURE_TENUE_MENAGE) > 0) {
                Float forfaitTenueMenage = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(CalculContext.Attribut.CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE)).getValeurCourante());
                donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_ENFANT,
                        forfaitTenueMenage
                                + donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE_ENFANT)));
            }

            float somme = 0;
            for (String champ : StrategieFinalRevenuActiviteLucrativeEnfant.champsAjoute) {
                somme += donnee.getValeurEnfant(champ);
            }

            // autres champs à déduire systématiquement
            for (String champ : StrategieFinalRevenuActiviteLucrativeEnfant.champsASupprimer) {
                somme -= donnee.getValeurEnfant(champ);
            }

            if (somme < 0.00f) {
                somme = 0;
            }

            // ajout du revenu de l'activité lucrative arrondi si IJAJ ET pas d'activite lucrative simultanément
            float revenuPrivilegie = somme;
            TupleDonneeRapport tupleActiviteLucrativeRevenuPrivilegie;

            revenuPrivilegie = Math.round(somme * TAUX_REVENU_ACTIVITE_LUCRATIVE_ENFANT);

            tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE_ENFANT, revenuPrivilegie);
            tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_ENFANT_LEGENDE);

            donnee.addEnfantTuple(tupleActiviteLucrativeRevenuPrivilegie);

            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_ENFANT, somme));

        }
    }

}
