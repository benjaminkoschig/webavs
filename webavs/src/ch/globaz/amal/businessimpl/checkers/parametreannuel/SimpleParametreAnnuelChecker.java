/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.parametreannuel;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author dhi
 * 
 */
public class SimpleParametreAnnuelChecker extends AmalAbstractChecker {

    /**
     * Contrôle de la validité du paramètre lors de sa création Tous les champs doivent être renseignés
     * 
     * @param param
     */
    public static void checkForCreate(SimpleParametreAnnuel param) {
        // Contrôle que les paramètres ne soient pas vide
        SimpleParametreAnnuelChecker.checkMandatory(param);
        SimpleParametreAnnuelChecker.checkIntegrity(param);
    }

    /**
     * Contrôle de la validité du paramètre pour sa suppression Actuellement, aucun contrôle
     * 
     * @param param
     */
    public static void checkForDelete(SimpleParametreAnnuel param) {
        // Pas de contrôle particulier

    }

    /**
     * Contrôle de la validité du paramètre pour sa mise à jour Tous les champs doivent être renseignés
     * 
     * @param param
     */
    public static void checkForUpdate(SimpleParametreAnnuel param) {
        // Contrôle que les paramètres ne soient pas vide
        SimpleParametreAnnuelChecker.checkMandatory(param);
        SimpleParametreAnnuelChecker.checkIntegrity(param);
    }

    /**
     * Contrôle de l'intégrité du paramètre Actuellement, aucun test d'intégrité
     * 
     * @param param
     */
    private static void checkIntegrity(SimpleParametreAnnuel param) {

    }

    /**
     * Contrôle des champs qui doivent être présents
     * 
     * @param param
     */
    private static void checkMandatory(SimpleParametreAnnuel param) {
        if (JadeStringUtil.isBlankOrZero(param.getAnneeParametre())) {
            JadeThread.logError(param.getClass().getName(), "amal.parametreAnnuel.anneeParametre.mandatory");
        }
        if (JadeStringUtil.isBlankOrZero(param.getCodeTypeParametre())) {
            JadeThread.logError(param.getClass().getName(), "amal.parametreAnnuel.codeTypeParametre.mandatory");
        }
        if (JadeStringUtil.isBlank(param.getValeurParametre())
                && JadeStringUtil.isBlank(param.getValeurParametreString())) {
            JadeThread.logError(param.getClass().getName(), "amal.parametreAnnuel.valeurs.mandatory");
        }
    }

}
