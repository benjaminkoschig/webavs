package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere;

import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.TypeRenteMap;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategiesFinalisationFactory;
import globaz.jade.client.util.JadeDateUtil;

import java.util.Objects;

public class UtilFortune {

    /**
     * M�thode qui retourne si le seuil a �t� atteint pour le calcul en param�tre
     *
     * @param donnee  du plan de calcul
     * @param context
     * @return true si le seuil est atteint
     * @throws CalculException
     */
    public static boolean isRefusFortune(TupleDonneeRapport donnee, CalculContext context) throws CalculException {
        boolean refusForce = false;
        if (isReformeApplicableDroit(context)) {
            float totalFortune;
            boolean isRenteEnfant = isRenteEnfant(donnee, context);
            if (donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION)
                    && (!(Boolean) context.get(CalculContext.Attribut.IS_FRATRIE)) || isRenteEnfant) {
                totalFortune = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION);
            } else {
                totalFortune = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL);
            }
            int nbParent = (Integer) context.get(CalculContext.Attribut.NB_PARENTS);
            float seuil;
            if ((Boolean) context.get(CalculContext.Attribut.IS_FRATRIE) || isRenteEnfant) {
                seuil = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(CalculContext.Attribut.CS_REFORME_SEUIL_FORTUNE_ENFANT)).getValeurCourante());
            } else if (nbParent == 2) {
                seuil = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(CalculContext.Attribut.CS_REFORME_SEUIL_FORTUNE_COUPLE)).getValeurCourante());
            } else {
                seuil = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(CalculContext.Attribut.CS_REFORME_SEUIL_FORTUNE_SEUL)).getValeurCourante());
            }
            if (totalFortune >= seuil) {
                refusForce = true;
                donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REFUS_SEUIL_FORTUNE));
            }
        }
        return refusForce;
    }

    private static boolean isRenteEnfant(TupleDonneeRapport donnee, CalculContext context) throws CalculException {

        // Si le requerant a une rente faisant partie de la liste des rentes avec besoin vitaux indiff�rents
        if (TypeRenteMap.listeCsRenteEnfantBesoinsVitauxIndifs.contains(donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT))) {
            return true;
        }

        int nbEnfantsMoins11 = (Integer) context.get(CalculContext.Attribut.NB_ENFANTS_INF_11);
        int nbEnfantsPlusOuEgal11 = (Integer) context.get(CalculContext.Attribut.NB_ENFANTS_EGAL_SUP_11);

        boolean enfantInstitutionOuBesoinVitaux = TypeRenteMap.listeCsRenteEnfantInstitutionBesoinsVitaux.contains(donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT));
        boolean isInstitution = (donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL).equals("null")) ? false : Float.valueOf(donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL)) > 0;
        boolean isBesoinVitauxEnfant = (nbEnfantsMoins11 != 0) || (nbEnfantsPlusOuEgal11 != 0);

        // Si le requerant poss�de une autre rente enfant, on va faire attention s'il poss�de des besoins vitaux adultes ou enfant, ou alors s'il est en Home/Institution
        return ((enfantInstitutionOuBesoinVitaux && isInstitution) || (enfantInstitutionOuBesoinVitaux && isBesoinVitauxEnfant));
    }

    /**
     * M�thode qui retourne si le seuil a �t� atteint pour le calcul en param�tre
     *
     * @param donnee  du plan de calcul
     * @return true si le seuil est atteint
     * @throws CalculException
     */
    public static boolean isRefusFortunePopUp(TupleDonneeRapport donnee) throws CalculException {

        Float totalFortune = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION);
        float seuil;
        int seuilInt = 40000;
        seuil = Float.parseFloat(String.valueOf(seuilInt));
        if (totalFortune > seuil) {
            return true;
        }
        return false;
    }

    /**
     * M�thode qui d�termine si le seuil de fortune a �t� atteint pour les donn�es de l'enfant pass�es en param�tre
     *
     * @param donnee  donn�es de l'enfant uniquement
     * @param context
     * @return true si le seuil est atteint
     * @throws CalculException
     */
    public static boolean isRefusChildFortune(TupleDonneeRapport donnee, CalculContext context) throws CalculException {
        boolean refusForce = false;
        if (isReformeApplicableDroit(context)) {
            TupleDonneeRapport clone = donnee.getClone();
            String dateValidite = (String) context.get(CalculContext.Attribut.DATE_DEBUT_PERIODE);

            for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryFortunePersonne().getStrategies()) {
                strategie.calcule(clone, context, JadeDateUtil.getGlobazDate(dateValidite));
            }

            float totalFortune;
            if (clone.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION)) {
                totalFortune = clone.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION);
            } else {
                totalFortune = clone.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL);
            }

            float seuil = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_REFORME_SEUIL_FORTUNE_ENFANT)).getValeurCourante());

            if (totalFortune >= seuil) {
                refusForce = true;
            }
        }
        return refusForce;
    }

    /**
     * M�thode qui retourne si le calcul du seuil de fortune la r�forme doit �tre appliqu� ou non
     *
     * @param context
     * @return booleen indiquant d'appliquer le calcul ou non
     * @throws CalculException
     */
    private static boolean isReformeApplicableDroit(CalculContext context) {
         return context.contains(CalculContext.Attribut.REFORME);
    }
}
