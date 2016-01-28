/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.parametreapplication;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author dhi
 * 
 */
public class SimpleParametreApplicationChecker extends AmalAbstractChecker {

    /**
     * Contrôle de la validité du paramètre lors de sa création Tous les champs doivent être renseignés
     * 
     * @param param
     */
    public static void checkForCreate(SimpleParametreApplication param) {
        // Contrôle que les paramètres ne soient pas vide
        SimpleParametreApplicationChecker.checkMandatory(param);
        SimpleParametreApplicationChecker.checkIntegrity(param);
    }

    /**
     * Contrôle de la validité du paramètre pour sa suppression Actuellement, aucun contrôle
     * 
     * @param param
     */
    public static void checkForDelete(SimpleParametreApplication param) {
        // Pas de contrôle particulier

    }

    /**
     * Contrôle de la validité du paramètre pour sa mise à jour Tous les champs doivent être renseignés
     * 
     * @param param
     */
    public static void checkForUpdate(SimpleParametreApplication param) {
        // Contrôle que les paramètres ne soient pas vide
        SimpleParametreApplicationChecker.checkMandatory(param);
        SimpleParametreApplicationChecker.checkIntegrity(param);
    }

    /**
     * Contrôle de l'intégrité du paramètre Actuellement, aucun test d'intégrité
     * 
     * @param param
     */
    private static void checkIntegrity(SimpleParametreApplication param) {

    }

    /**
     * Contrôle des champs qui doivent être présents
     * 
     * @param param
     */
    private static void checkMandatory(SimpleParametreApplication param) {
        if (JadeStringUtil.isEmpty(param.getCsTypeParametre())) {
            JadeThread.logError(param.getClass().getName(), "amal.parametreApplication.csTypeParametre.mandatory");
        }
        if (JadeStringUtil.isEmpty(param.getValeurParametre())) {
            JadeThread.logError(param.getClass().getName(), "amal.parametreApplication.valeurParametre.mandatory");
        }
    }

}
