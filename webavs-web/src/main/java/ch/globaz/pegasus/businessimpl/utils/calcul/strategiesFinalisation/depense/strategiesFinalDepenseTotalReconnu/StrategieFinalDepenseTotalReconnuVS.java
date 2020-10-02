package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu;

import globaz.jade.log.JadeLogger;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.TypeRenteMap;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;

/**
 * extend de la stratégie de base, override du calcul des dépenses personnelles
 * 
 * @author cel
 * 
 */
public class StrategieFinalDepenseTotalReconnuVS extends StrategieFinalDepenseTotalReconnu {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        String[] champATraiter = champs;
        float nbHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);
        if (nbHomes == 0) {

            // calcul de la couverture des besoins vitaux
            float couvertureBesoinsVitaux = calculeCouvertureBesoinsVitaux(context);
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
                    couvertureBesoinsVitaux));
        } else {

            // calcul de l'argent de poche/dépense personnelle
            float depensesPersonnees = calculeDepensesPersonnelles(context, donnee);
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL,
                    depensesPersonnees));
    }

        // calcul des totaux
        float somme = 0;
        float revenuAgricole = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE);

        // Si pas revenu indépendant agricole
        if (revenuAgricole != 0) {
            champATraiter = champsWithRevenuAgricole;
        }
        for (String champ : champATraiter) {
            somme += donnee.getValeurEnfant(champ);
        }

        float fraisImmoPlafonne = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE);
        // donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_REVENU));
        // donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE,
        // fraisImmoPlafonne));
        somme += fraisImmoPlafonne;

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL, somme));

        checkSejourMoisPartiel(context, donnee);

    }

    @Override
    float calculeDepensesPersonnelles(CalculContext context, TupleDonneeRapport donnee) throws CalculException {

        String csTypeRentePC = null;

        try {
            csTypeRentePC = donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT);

            // fallback sur le conjoint
            if (csTypeRentePC == null) {
                csTypeRentePC = donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_CONJOINT);

                if (null == csTypeRentePC) {
                    // Si toujours null, on va rechercher le type de rente du requérant dans le contete
                    csTypeRentePC = (String) context.get(Attribut.TYPE_RENTE_REQUERANT);
                }

            }

        } catch (NullPointerException e) {
            JadeLogger.info(this,
                    "The value csTypeRentePC comming from calculator is null. Meaning the people is not in a home.");
        }

        // valeur par defaut 0, pas de depense, dans le cas ou la personne n'est
        // pas dans un home
        Float depensesPersonnelles = 0f;

        Attribut attribut = null;

        if (TypeRenteMap.listeCsRenteSurvivant.contains(csTypeRentePC)
                || TypeRenteMap.listeCsRenteInvalidite.contains(csTypeRentePC)) {
            attribut = Attribut.CS_ARGENT_POCHE_HOME_AI_ANNUEL;
        } else if (TypeRenteMap.listeCsRenteVieillesse.contains(csTypeRentePC)) {
            attribut = Attribut.CS_ARGENT_POCHE_HOME_AVS_ANNUEL;
        }

        try {
            depensesPersonnelles = Float.parseFloat(((ControlleurVariablesMetier) context.get(attribut))
                    .getValeurCourante());
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            throw new CalculBusinessException(
                    "pegasus.calcul.strategie.final.depenseTotalReconnu.csTypeRentePC.integrity", csTypeRentePC,
                    (String) context.get(Attribut.DATE_DEBUT_PERIODE));
        }
        return depensesPersonnelles;
    }
}
