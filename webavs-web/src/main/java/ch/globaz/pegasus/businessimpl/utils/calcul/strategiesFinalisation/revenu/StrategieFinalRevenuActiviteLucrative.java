package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuActiviteLucrative implements StrategieCalculFinalisation {
    private final static String[] champsAjoute = { IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_HYPOTHETIQUE };

    private final static String[] champsASupprimer = { IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_SOCIALES,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_LPP,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_FRAIS_OBTENTION_REVENU,
            IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        final float TAUX_REVENU_ACTIVITE_LUCRATIVE = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getValeurCourante());
        final String TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE = ((ControlleurVariablesMetier) context
                .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getLegendeCourante();

        float deductionForfaitaireRevenu = 0f;

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

        // déduction forfaitaire
        if (somme > 0) {
            int nbPersonnes = (Integer) context.get(Attribut.NB_PERSONNES);
            if (nbPersonnes > 1) {
                deductionForfaitaireRevenu = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE)).getValeurCourante());
            } else {
                deductionForfaitaireRevenu = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES)).getValeurCourante());
            }
            donnee.addEnfantTuple(new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU, deductionForfaitaireRevenu));
        }

        // autres champs à déduire systématiquement
        for (String champ : StrategieFinalRevenuActiviteLucrative.champsASupprimer) {
            somme -= donnee.getValeurEnfant(champ);
        }

        if (somme < 0.00f) {
            somme = 0;
        }

        // ajout du revenu de l'activité lucrative
        float revenuPrivilegie = Math.round(somme * TAUX_REVENU_ACTIVITE_LUCRATIVE);
        // sommeRevenuCommun += revenuPrivilegie;

        // Ajout revenu privilgié
        // if (revenuPrivilegie > 0.0f) {
        TupleDonneeRapport tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE, revenuPrivilegie);
        tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE);
        donnee.addEnfantTuple(tupleActiviteLucrativeRevenuPrivilegie);
        // }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL_NON_PLAFFONNE, somme));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL, somme));

    }

}
