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
     * Contr�le de la validit� du param�tre lors de sa cr�ation Tous les champs doivent �tre renseign�s
     * 
     * @param param
     */
    public static void checkForCreate(SimpleParametreAnnuel param) {
        // Contr�le que les param�tres ne soient pas vide
        SimpleParametreAnnuelChecker.checkMandatory(param);
        SimpleParametreAnnuelChecker.checkIntegrity(param);
    }

    /**
     * Contr�le de la validit� du param�tre pour sa suppression Actuellement, aucun contr�le
     * 
     * @param param
     */
    public static void checkForDelete(SimpleParametreAnnuel param) {
        // Pas de contr�le particulier

    }

    /**
     * Contr�le de la validit� du param�tre pour sa mise � jour Tous les champs doivent �tre renseign�s
     * 
     * @param param
     */
    public static void checkForUpdate(SimpleParametreAnnuel param) {
        // Contr�le que les param�tres ne soient pas vide
        SimpleParametreAnnuelChecker.checkMandatory(param);
        SimpleParametreAnnuelChecker.checkIntegrity(param);
    }

    /**
     * Contr�le de l'int�grit� du param�tre Actuellement, aucun test d'int�grit�
     * 
     * @param param
     */
    private static void checkIntegrity(SimpleParametreAnnuel param) {

    }

    /**
     * Contr�le des champs qui doivent �tre pr�sents
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
