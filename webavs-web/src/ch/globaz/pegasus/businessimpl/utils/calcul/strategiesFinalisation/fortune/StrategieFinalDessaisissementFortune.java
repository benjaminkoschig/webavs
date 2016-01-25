/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune;

import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.dessaisissement.DessaisissementUtils;
import ch.globaz.pegasus.businessimpl.utils.dessaisissement.DessaisissementUtils.Fortune;

/**
 * @author ECO
 * 
 */
public class StrategieFinalDessaisissementFortune implements StrategieCalculFinalisation {

    // private static final int MONTANT_DEVALUATION = 10000;

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation#calcule(ch.globaz
     * .pegasus.businessimpl.utils.calcul.TupleDonneeRapport, ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext,
     * java.util.Date)
     */
    /**
     * !!!! METHODE RESULTANT DU REFACTORING DU CODE D'AMORTISSEMENT DES BIENS DESSAISIS !!!!
     */
    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        TupleDonneeRapport tupleFortunes = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_FORTUNES_DESSAISIES);
        float somme = 0f;
        if (tupleFortunes != null) {

            float sommeInitiale = 0f;

            // construit une liste des données déssaisies à amortir
            List<Fortune> fortunes = new ArrayList<Fortune>();

            // récupère la date la plus ancienne
            Date dateDebutCalcul = null;
            for (TupleDonneeRapport fortune : tupleFortunes.getEnfants().values()) {
                // TODO verifier avec ECO le format de la date
                Date dateFortune = JadeDateUtil.getGlobazDate("01."
                        + fortune.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_DATE));
                float montant = fortune.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_MONTANT);
                fortunes.add(new Fortune(dateFortune, montant));
                if ((dateDebutCalcul == null) || dateFortune.before(dateDebutCalcul)) {
                    dateDebutCalcul = dateFortune;
                }
                sommeInitiale += montant;
            }

            // récupère la liste historique des valeurs de la variable métier d'amortissement annuel
            TreeMap<Long, String> amortissements = ((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE)).getVariable()
                    .getVariablesMetiers();

            somme = DessaisissementUtils.calculeAmortissement(dateDebut, dateDebutCalcul, fortunes, amortissements);

        }
        /*
         * Le calcul des interets à été déplacée ici pour calculer, non pas sur le montant initial, mais le montant
         * dévaluée
         */

        float tauxInteret = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(CalculContext.Attribut.CS_2091_DPC)).getValeurCourante()) / 100f;

        float sommeInterets = somme * tauxInteret;

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL, sommeInterets));

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL, somme));
    }

    /**
     * !!!! METHODES DATANT D'AVANT REFACTORING DU CODE D'AMORTISSEMENT DES BIENS DESSAISIS !!!!
     */
    // public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
    //
    // TupleDonneeRapport tupleFortunes = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_FORTUNES_DESSAISIES);
    // float somme = 0f;
    // if (tupleFortunes != null) {
    //
    // float sommeInitiale = 0f;
    //
    // List<Fortune> fortunes = new ArrayList<Fortune>();
    //
    // // récupère la date la plus ancienne
    // Date dateDebutCalcul = null;
    // for (TupleDonneeRapport fortune : tupleFortunes.getEnfants().values()) {
    // // TODO verifier avec ECO le format de la date
    // Date dateFortune = JadeDateUtil.getGlobazDate("01."
    // + fortune.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_DATE));
    // float montant = fortune.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_FORTUNE_DESSAISIE_MONTANT);
    // fortunes.add(new Fortune(dateFortune, montant));
    // if ((dateDebutCalcul == null) || dateFortune.before(dateDebutCalcul)) {
    // dateDebutCalcul = dateFortune;
    // }
    // sommeInitiale += montant;
    // }
    //
    // /*
    // * TODO déplacer pour reprendre le montant avec devaluation float tauxInteret = (Float)
    // * context.get(CalculContext.Attribut.CS_2091_DPC) / 100f;
    // *
    // * float sommeInterets = sommeInitiale * tauxInteret; donnee.addEnfantTuple(new
    // * TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL, sommeInterets));
    // */
    //
    // // calcul des montants
    // Calendar calPeriode = Calendar.getInstance();
    // calPeriode.setTime(dateDebut);
    // Calendar calFortune = Calendar.getInstance();
    // calFortune.setTime(dateDebutCalcul);
    // float sommeNonPlafonnable = 0;
    // for (int annee = calFortune.get(Calendar.YEAR); annee <= calPeriode.get(Calendar.YEAR); annee++) {
    // // float montantAmortissementAnnuelDessaisissementFortune = this
    // // .getMontantAmortissementDessaisissementFortune(annee, context);
    // somme = Math.max(somme - this.getMontantAmortissementDessaisissementFortune(annee, context), 0);
    // somme += sommeNonPlafonnable;
    //
    // // pour tous les dessaisissements
    // sommeNonPlafonnable = 0f;
    // for (Fortune fortune : fortunes) {
    // if (fortune.annee == annee) {
    // /*
    // * if (fortune.mois < 6) { somme += fortune.montantInitial; } else { sommeNonPlafonnable +=
    // * fortune.montantInitial; }
    // */
    // sommeNonPlafonnable += fortune.montantInitial;
    // }
    //
    // }
    // }
    // somme += sommeNonPlafonnable;
    // // TODO A VOIR
    // // somme += sommeInterets;
    // }
    // /*
    // * Le calcul des interets à été déplacée ici pour calculer, non pas sur le montant initial, mais le montant
    // * dévaluée
    // */
    // // float tauxInteret = (Float) context.get(CalculContext.Attribut.CS_2091_DPC) / 100f;
    //
    // float tauxInteret = Float.parseFloat(((ControlleurVariablesMetier) context
    // .get(CalculContext.Attribut.CS_2091_DPC)).getValeurCourante()) / 100f;
    //
    // float sommeInterets = somme * tauxInteret;
    //
    // donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL, sommeInterets));
    //
    // donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_DESS_TOTAL, somme));
    // }
    //
    // private float getMontantAmortissementDessaisissementFortune(int annee, CalculContext ctx)
    // throws NumberFormatException, CalculException {
    // int month = 1;
    //
    // return Float.parseFloat((((ControlleurVariablesMetier) ctx
    // .get(CalculContext.Attribut.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE)).getVariable()
    // .getValeurDepuisDate(month, annee)));
    // }

    /**
     * FIN DES METHODES D'AVANT LE REFACTORING
     */

}
