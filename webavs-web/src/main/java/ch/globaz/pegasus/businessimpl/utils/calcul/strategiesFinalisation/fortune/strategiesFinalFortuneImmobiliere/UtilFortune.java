package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategiesFinalisationFactory;
import globaz.jade.client.util.JadeDateUtil;

public class UtilFortune {

    /**
     * Méthode qui retourne si le seuil a été atteint pour le calcul en paramètre
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
            if (donnee.containsValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION) && !(Boolean) context.get(CalculContext.Attribut.IS_FRATRIE)) {
                totalFortune = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL_AVANT_FRACTION);
            } else {
                totalFortune = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL);
            }
            int nbParent = (Integer) context.get(CalculContext.Attribut.NB_PARENTS);
            float seuil;
            if ((Boolean) context.get(CalculContext.Attribut.IS_FRATRIE)) {
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

    /**
     * Méthode qui retourne si le seuil a été atteint pour le calcul en paramètre
     *
     * @param donnee  du plan de calcul
     * @param context
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
     * Méthode qui détermine si le seuil de fortune a été atteint pour les données de l'enfant passées en paramètre
     *
     * @param donnee  données de l'enfant uniquement
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
     * Méthode qui retourne si le calcul du seuil de fortune la réforme doit être appliqué ou non
     *
     * @param context
     * @return booleen indiquant d'appliquer le calcul ou non
     * @throws CalculException
     */
    private static boolean isReformeApplicableDroit(CalculContext context) throws CalculException {
//        String dateReforme = "";
//        String dateValidite = (String) context.get(CalculContext.Attribut.DATE_DEBUT_PERIODE);
//        try {
//            dateReforme = EPCProperties.DATE_REFORME_PC.getValue();
//        } catch (PropertiesException e) {
//            throw new CalculException("Propriété date reforme pc manquante", e);
//        }
//        boolean isReforme = false;
//        try {
//            isReforme = EPCProperties.REFORME_PC.getBooleanValue();
//        } catch (PropertiesException e) {
//            throw new CalculException("Propriété reforme pc manquante", e);
//        }
//        return isReforme                                                                        // si réforme pc active
//        && (context.contains(CalculContext.Attribut.REFORME)                                    // si dans le calcul de la réforme
//        || (!dateReforme.isEmpty() && JadeDateUtil.isDateBefore(dateReforme, dateValidite)));   // ou si date de début du droit après la date de début de la réforme
        return context.contains(CalculContext.Attribut.REFORME);
    }
}
