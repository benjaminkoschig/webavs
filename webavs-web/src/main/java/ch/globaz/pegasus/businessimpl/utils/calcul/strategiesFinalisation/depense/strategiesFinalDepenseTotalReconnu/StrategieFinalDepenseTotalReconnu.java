package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.log.JadeLogger;

import java.util.Calendar;
import java.util.Date;

public class StrategieFinalDepenseTotalReconnu implements StrategieCalculFinalisation {
    protected final static String[] champs = { IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_LONGUE_DUREE,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAIS_GARDE_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_SEJOUR_MOIS_PARTIEL_TOTAL};

    protected final static String[] champsWithRevenuAgricole = { IPCValeursPlanCalcul.CLE_DEPEN_BES_VITA_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_DEPPERSO_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_PENSVERS_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TOTAL, IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL,
            IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_LONGUE_DUREE };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        String[] champATraiter = StrategieFinalDepenseTotalReconnu.champs;
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
            champATraiter = StrategieFinalDepenseTotalReconnu.champsWithRevenuAgricole;
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

        checkSejourMoisPartiel(context, donnee);

    }

    protected float calculeCouvertureBesoinsVitaux(CalculContext context) throws CalculException {
        float result = 0;
        if(context.contains(Attribut.REFORME)){
            return calculConvertureBesoinsVitauxReformePC(context);
        }else {

            int nbEnfants = (Integer) context.get(Attribut.NB_ENFANTS);

            if (!((Boolean) context.get(Attribut.IS_FRATRIE))) { //NOSONAR

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
        }

        return result;
    }

    private float calculConvertureBesoinsVitauxReformePC(CalculContext context) throws CalculException {
        float result = 0;

        int nbEnfantsMoins11 = (Integer) context.get(Attribut.NB_ENFANTS_INF_11);
        int nbEnfantsPlusOuEgal11 = (Integer) context.get(Attribut.NB_ENFANTS_EGAL_SUP_11);
        float montantIntermediaire;
        if (!((Boolean) context.get(Attribut.IS_FRATRIE))) {  //NOSONAR

            int nbParents = (Integer) context.get(Attribut.NB_PARENTS);
            result = (nbParents == 2 ? Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.CS_BESOINS_VITAUX_COUPLES)).getValeurCourante()) : Float
                    .parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_BESOINS_VITAUX_CELIBATAIRES))
                            .getValeurCourante()));
        }
        if(nbEnfantsPlusOuEgal11>0){
            montantIntermediaire = 0.f;

            montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_5))
                    .getValeurCourante()) * Math.max(0, nbEnfantsPlusOuEgal11 - 4);

            switch (Math.min(nbEnfantsPlusOuEgal11, 4)) {
                case 4:
                    montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4)).getValeurCourante());
                case 3:
                    montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4)).getValeurCourante());
                case 2:
                    montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2)).getValeurCourante());
                case 1:
                    montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2)).getValeurCourante());
                default:
                    break;
            }
            result+=montantIntermediaire;
        }
        if(nbEnfantsMoins11>0){
            montantIntermediaire = 0.f;

            for(int i = nbEnfantsPlusOuEgal11+1; i<=(nbEnfantsPlusOuEgal11+nbEnfantsMoins11);i++){
                switch (Math.min(i, 5)) {
                    case 5 :
                        montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context.get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_5))
                                .getValeurCourante()) ;
                        break;
                    case 4:
                        montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_4)).getValeurCourante());
                        break;
                    case 3:
                        montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_3)).getValeurCourante());
                        break;
                    case 2:
                        montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_2)).getValeurCourante());
                        break;
                    case 1:
                        montantIntermediaire += Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_1)).getValeurCourante());
                        break;
                    default:
                        break;
                }
            }

            result+=montantIntermediaire;
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    float calculeDepensesPersonnelles(CalculContext context, TupleDonneeRapport donnee) throws CalculException {
        String csTypeChambre = null;
        try {
            csTypeChambre = donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_DEPENSE_CS_TYPE_CHAMBRE);
        } catch (NullPointerException e) {
            JadeLogger.info(this,
                    "The value csTypeChambre comming from calculator is null. Meaning the people is not in a home.");

        }

        // valeur par defaut 0, pas de depense, dans le cas ou la personne n'est
        // pas dans un home
        Float depensesPersonnelles = 0f;

        try {
            depensesPersonnelles = ArgentDePocheHomeResolver.getMontantForCalcul(context, csTypeChambre);
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            throw new CalculBusinessException(
                    "pegasus.calcul.strategie.final.depenseTotalReconnu.csTypeChambre.integrity", csTypeChambre,
                    (String) context.get(Attribut.DATE_DEBUT_PERIODE));
        }
        return depensesPersonnelles * 12;
    }

    protected void checkSejourMoisPartiel(CalculContext context, TupleDonneeRapport donnee) throws CalculException{
            TupleDonneeRapport tupleMoisPartiel = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL);
            if (tupleMoisPartiel != null) {
                int nbJourRequerant = 0;
                int nbJourConjoint = 0;
                nbJourRequerant += tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_NOMBRE_JOURS_REQUERANT);
                nbJourConjoint += tupleMoisPartiel.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_SEJOUR_MOIS_PARTIEL_NOMBRE_JOURS_CONJOINT);

                String dateDebutPeriode = (String) context.get(Attribut.DATE_DEBUT_PERIODE);
                int nbrJourMax = JadeDateUtil.getGlobazCalendar(JadeDateUtil.getLastDateOfMonth(dateDebutPeriode)).getActualMaximum(Calendar.DAY_OF_MONTH);
                if(nbJourRequerant > nbrJourMax){
                    throw new CalculBusinessException(
                            "pegasus.calcul.strategie.final.depenseTotalReconnu.nbjoursSejourPartiel.integrity", Integer.toString(nbJourRequerant));
                }
                if(nbJourConjoint > nbrJourMax){
                    throw new CalculBusinessException(
                            "pegasus.calcul.strategie.final.depenseTotalReconnu.nbjoursSejourPartiel.conjoint.integrity", Integer.toString(nbJourConjoint));
                }
            }
    }

}
