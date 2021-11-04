package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere;

import java.util.Date;


import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;

public class StrategieFinalFortuneImmobiliere201101 implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        float sommeHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);

        Float deductionForfaitaire = 0f;
        // Conditions metiers, a false par défaut
        boolean conditionsMetiersRemplis = false;

        float fortune_bien_immo_principal = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE);
        float fortune_bien_immo_secondaire = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE);
        float fortune_bien_immo_non_habitable = donnee
                .getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES);
        float somme = fortune_bien_immo_principal;

        // Conditions métiers pour l'application de la bonne variable métier
        // nombre de parents, si pas 2 , conditions metiers pas remplis
        int nbreParents = (Integer) context.get(Attribut.NB_PARENTS);
        // if (nbreParents == 2) {
        conditionsMetiersRemplis = getIsConditionsMetiersRemplis(donnee);
        // }

        if (fortune_bien_immo_principal > 0) {
            // Si les conditions metiers sont ok, non applique la variable métier spécifique
            if (conditionsMetiersRemplis) {
                deductionForfaitaire = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE_HOME_API)).getValeurCourante());
            } else {
                deductionForfaitaire = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE)).getValeurCourante());
            }

            somme -= deductionForfaitaire;

        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT,
                UtilStrategieFinalFortuneImmobiliere.plafonneValeurBiensImmoDeduit(somme)));
        if(context.contains(Attribut.REFORME)){
            float fortune_bien_immo_principal_deduit_hypo = UtilStrategieFinalFortuneImmobiliere.plafondDeductionDetteHypo(UtilStrategieFinalFortuneImmobiliere.plafonneValeurBiensImmoDeduit(somme),donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY));
            somme = fortune_bien_immo_principal_deduit_hypo;
            float fortune_bien_immo_secondaire_deduit_hypo = UtilStrategieFinalFortuneImmobiliere.plafondDeductionDetteHypo(fortune_bien_immo_secondaire,donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED));
            somme += fortune_bien_immo_secondaire_deduit_hypo;
            float fortune_bien_immo_non_habitable_deduit_hypo = UtilStrategieFinalFortuneImmobiliere.plafondDeductionDetteHypo(fortune_bien_immo_non_habitable,donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED));
            somme += fortune_bien_immo_non_habitable_deduit_hypo;
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL, somme));
            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE,deductionForfaitaire));
            TupleDonneeRapport turpleSommeTotalParTypeBien = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_PRINCIPAL, fortune_bien_immo_principal_deduit_hypo);
            String label = CodeSystemUtil.getCodeSysteme(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL, Langues.getLangueDepuisCodeIso(BSessionUtil.getSessionFromThreadContext().getIdLangueISO())).getLibelle();
            turpleSommeTotalParTypeBien.setLegende(label);
            donnee.addEnfantTuple(turpleSommeTotalParTypeBien);
            turpleSommeTotalParTypeBien = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_PRINCIPAL, fortune_bien_immo_principal_deduit_hypo);
            turpleSommeTotalParTypeBien.setLegende(label);
            donnee.addEnfantTuple(turpleSommeTotalParTypeBien);
            turpleSommeTotalParTypeBien = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_SECONDAIRE, fortune_bien_immo_secondaire_deduit_hypo);
            turpleSommeTotalParTypeBien.setLegende(label);
            donnee.addEnfantTuple(turpleSommeTotalParTypeBien);
            turpleSommeTotalParTypeBien = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_INTER_FORTUNE_IMMOBILIER_TOTAL_NON_HABITABLE, fortune_bien_immo_non_habitable_deduit_hypo);
            turpleSommeTotalParTypeBien.setLegende(label);
            donnee.addEnfantTuple(turpleSommeTotalParTypeBien);


            if (donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_PRINCIPAL_PLAFONNEE)) {
                float montant = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY);
                donnee.addEnfantTuple(new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_REAL_PROPERTY, montant, "Plafonnée"));
            }

            if (donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_ANNEXE_PLAFONNEE)) {
                float montant = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED);
                donnee.addEnfantTuple(new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_SELF_INHABITED, montant, "Plafonnée"));
            }

            if (donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NON_HABITABLE_PLAFONNEE)) {
                float montant = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED);
                donnee.addEnfantTuple(new TupleDonneeRapport(
                        IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_NOT_HABITED, montant, "Plafonnée"));
            }
        }else{
            somme = UtilStrategieFinalFortuneImmobiliere.plafonneValeurBiensImmoDeduit(somme);
            somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABIT_PRINCIPALE);
            // somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_PRINCIPAL_DEDUIT);

            somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_NON_HABITABLES);

            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_TOTAL, somme));

            donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_DEDUCTION_FOFAITAIRE,
                    deductionForfaitaire));
        }



    }

    /**
     * Methode gérant les conditions métiers propores a cette donnée financières
     * 
     * @param donnee
     * @return boolean, etat de la condition métier
     */
    private boolean getIsConditionsMetiersRemplis(TupleDonneeRapport donnee) {
        // nombre de homes
        float sommeHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);
        // biens immo
        float biensImmo = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_FOR_IMMO_BIENS_IMMO_HABIT_PRINCIPALE);
        // api
        // float api = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI);
        float api = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_IS_API_ENCODE);
        boolean apiEncode = api != 0;

        // SI on a un home (somme des homes) ET un biens immo, OU au moins une API ET un biens immo
        if (((sommeHomes > 0.0f) && (biensImmo > 0.0f)) || ((apiEncode) && (biensImmo > 0.0f))) {
            return true;
        }
        return false;

    }
}
