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

        // déduction forfaitaire si IJAJ et activite lucrative simultanément
        if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)
                && isActiviteLucrative(donnee)) {
            donnee.addEnfantTuple(new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_DEDUCTION_FORFAITAIRE_REVENU, deductionForfaitaireRevenu));
        } else {
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

        if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)
                && isActiviteLucrative(donnee)) {
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

    /***
     * Méthode qui permet de savoir si on est dans un cas ou l'on a une activité lucrative
     * (K141106_001)
     * 
     * @param donnee
     * @return
     */
    private boolean isActiviteLucrative(TupleDonneeRapport donnee) {
        return (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_DEPENDANTE)
                || donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE) || donnee
                .getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE));
    }
}
