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
     * Contr�le de la validit� du param�tre lors de sa cr�ation Tous les champs doivent �tre renseign�s
     * 
     * @param param
     */
    public static void checkForCreate(SimpleParametreApplication param) {
        // Contr�le que les param�tres ne soient pas vide
        SimpleParametreApplicationChecker.checkMandatory(param);
        SimpleParametreApplicationChecker.checkIntegrity(param);
    }

    /**
     * Contr�le de la validit� du param�tre pour sa suppression Actuellement, aucun contr�le
     * 
     * @param param
     */
    public static void checkForDelete(SimpleParametreApplication param) {
        // Pas de contr�le particulier

    }

    /**
     * Contr�le de la validit� du param�tre pour sa mise � jour Tous les champs doivent �tre renseign�s
     * 
     * @param param
     */
    public static void checkForUpdate(SimpleParametreApplication param) {
        // Contr�le que les param�tres ne soient pas vide
        SimpleParametreApplicationChecker.checkMandatory(param);
        SimpleParametreApplicationChecker.checkIntegrity(param);
    }

    /**
     * Contr�le de l'int�grit� du param�tre Actuellement, aucun test d'int�grit�
     * 
     * @param param
     */
    private static void checkIntegrity(SimpleParametreApplication param) {

    }

    /**
     * Contr�le des champs qui doivent �tre pr�sents
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
