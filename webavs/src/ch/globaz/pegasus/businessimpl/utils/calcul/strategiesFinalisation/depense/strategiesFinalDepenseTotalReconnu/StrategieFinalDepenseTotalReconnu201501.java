package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu;

import globaz.jade.log.JadeLogger;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.ArgentDePocheHomeResolver;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalDepenseTotalReconnu201501 implements StrategieCalculFinalisation {

    private final static String[] champs = { IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL };

    private final static String[] champsWithRevenuAgricole = { IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        String[] champATraiter = StrategieFinalDepenseTotalReconnu201501.champs;
        float nbHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);
        if (nbHomes == 0) {

            // calcul de la couverture des besoins vitaux
            float couvertureBesoinsVitaux = calculeCouvertureBesoinsVitaux(context);
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
                    couvertureBesoinsVitaux));
        } else {

            // calcul de l'argent de poche/dépense personnelle
            float depensesPersonnees = calculeDepensesPersonnelles(context, donnee) * 12;
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL,
                    depensesPersonnees));
        }

        // calcul des totaux
        float somme = 0;
        float revenuAgricole = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_ACTIVITE_INDEPENDANTE_AGRICOLE);

        // Si pas revenu indépendant agricole
        if (revenuAgricole != 0) {
            champATraiter = StrategieFinalDepenseTotalReconnu201501.champsWithRevenuAgricole;
        }
        for (String champ : champATraiter) {
            somme += donnee.getValeurEnfant(champ);
        }

        float fraisImmoPlafonne = Math.min(donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL),
                donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_REVENU));
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_TOTAL_PLAFONNE,
                fraisImmoPlafonne));
        somme += fraisImmoPlafonne;

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_TOTAL, somme));

    }

    private float calculeCouvertureBesoinsVitaux(CalculContext context) throws CalculException {
        float result = 0;

        int nbEnfants = (Integer) context.get(Attribut.NB_ENFANTS);

        if (!((Boolean) context.get(Attribut.IS_FRATRIE))) {

            int nbParents = (Integer) context.get(Attribut.NB_PARENTS);
            result = (nbParents == 2 ? Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.CS_BESOINS_VITAUX_COUPLES)).getValeurCourante()) : Float
                    .parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_BESOINS_VITAUX_CELIBATAIRES))
                            .getValeurCourante()));

        } else {
            nbEnfants += 1;
        }

        // sommation des déductions par nombre d'enfant
        // calcule des déductions à partir du 5e enfant (règle 2.2.1.3-1)
        result += Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_BESOINS_VITAUX_5_ENFANTS))
                .getValeurCourante()) * Math.max(0, nbEnfants - 4);

        // l'ordre inverse et l'absence de break; est nécessaire pour calculer
        // les déductions de 2x(3,4e enfant)+2x(1,2e enfant)
        switch (Math.min(nbEnfants, 4)) {
            case 4:
                result += Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_BESOINS_VITAUX_4_ENFANTS)).getValeurCourante());
            case 3:
                result += Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_BESOINS_VITAUX_4_ENFANTS)).getValeurCourante());
            case 2:
                result += Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_BESOINS_VITAUX_2_ENFANTS)).getValeurCourante());
            case 1:
                result += Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_BESOINS_VITAUX_2_ENFANTS)).getValeurCourante());
            default:
                break;
        }

        return result;
    }

    private float calculeDepensesPersonnelles(CalculContext context, TupleDonneeRapport donnee)
            throws CalculBusinessException, CalculException {
        String csCategorieArgentDePocheTypeChambre = null;
        try {
            csCategorieArgentDePocheTypeChambre = donnee
                    .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_DEPENSE_CS_TYPE_ARGENT_DE_POCHE_TYPE_CHAMBRE);

            if (IPCTaxeJournaliere.CS_CATEGORIE_NON_DEFINIE.equals(csCategorieArgentDePocheTypeChambre)) {
                throw new CalculBusinessException(
                        "pegasus.calcul.strategie.final.depenseTotalReconnu.csTypeChambre.undefined",
                        csCategorieArgentDePocheTypeChambre);
            }
        } catch (NullPointerException e) {
            JadeLogger.info(this,
                    "The value csTypeChambre comming from calculator is null. Meaning the people is not in a home.");
        }

        // valeur par defaut 0, pas de depense, dans le cas ou la personne n'est
        // pas dans un home

        Float depensesPersonnelles = 0f;

        try {
            depensesPersonnelles = ArgentDePocheHomeResolver.getMontantForCalcul(context,
                    csCategorieArgentDePocheTypeChambre);
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            throw new CalculBusinessException(
                    "pegasus.calcul.strategie.final.depenseTotalReconnu.csTypeChambre.integrity",
                    csCategorieArgentDePocheTypeChambre, (String) context.get(Attribut.DATE_DEBUT_PERIODE));
        }

        return depensesPersonnelles;
    }

}
