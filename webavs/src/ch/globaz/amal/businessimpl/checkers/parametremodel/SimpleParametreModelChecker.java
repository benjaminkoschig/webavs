/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.parametremodel;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.amal.business.models.parametremodel.SimpleParametreModel;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author dhi
 * 
 */
public class SimpleParametreModelChecker extends AmalAbstractChecker {

    /**
     * Validation des param�tres de mod�le de document lors de sa cr�ation Les champs doivent �tre renseign�s Des
     * param�tres n'existent pas encore pour le m�me id formule
     * 
     * @param parametreModel
     */
    public static void checkForCreate(SimpleParametreModel parametreModel) {
        // Contr�le que les champs soient renseign�s
        SimpleParametreModelChecker.checkMandatory(parametreModel);
        // Contr�le qu'un param�tre n'est pas d�j� existant + dates d�but/fin
        SimpleParametreModelChecker.checkIntegrity(parametreModel);
    }

    /**
     * Validation des param�tres d'un mod�le de document lors de sa suppression
     * 
     * PAS DE CONTR�LE PARTICULIER
     * 
     * @param parametreModel
     */
    public static void checkForDelete(SimpleParametreModel parametreModel) {
        // Pas de contr�le particulier
    }

    /**
     * Validation des param�tres de mod�le de document lors de sa mise � jour Les champs doivent �tre renseign�s Des
     * param�tres n'existent pas encore pour le m�me id formule
     * 
     * @param parametreModel
     */
    public static void checkForUpdate(SimpleParametreModel parametreModel) {
        // Contr�le que les champs soient renseign�s
        SimpleParametreModelChecker.checkMandatory(parametreModel);
        // Contr�le qu'un param�tre n'est pas d�j� existant + dates d�but/fin
        SimpleParametreModelChecker.checkIntegrity(parametreModel);
    }

    /**
     * Contr�le les dates de validit�s du model et contr�le qu'un autre param�tre n'existe pas avec un m�me id formule
     * 
     * @param parametreModel
     */
    private static void checkIntegrity(SimpleParametreModel parametreModel) {
        // TODO : SIMPLEPARAMETREMODELSEARCH
        if (!parametreModel.getAnneeValiditeFin().equals("0")) {
            int iYearDebut = Integer.parseInt(parametreModel.getAnneeValiditeDebut());
            int iYearFin = Integer.parseInt(parametreModel.getAnneeValiditeFin());
            if (iYearFin < iYearDebut) {
                JadeThread.logError(parametreModel.getClass().getName(),
                        "amal.simpleParametreModel.anneeValidite.integrity");
            }
        }
    }

    /**
     * contr�le que l'ensemble des champs soit renseign�
     * 
     * @param parametreModel
     */
    private static void checkMandatory(SimpleParametreModel parametreModel) {
        if (JadeStringUtil.isBlank(parametreModel.getAnneeValiditeDebut())) {
            JadeThread.logError(parametreModel.getClass().getName(),
                    "amal.simpleParametreModel.anneeValiditeDebut.mandatory");
        }
        if (JadeStringUtil.isBlank(parametreModel.getAnneeValiditeFin())) {
            JadeThread.logError(parametreModel.getClass().getName(),
                    "amal.simpleParametreModel.anneeValiditeFin.mandatory");
        }
        if (JadeStringUtil.isBlank(parametreModel.getCodeTraitementDossier())) {
            JadeThread.logError(parametreModel.getClass().getName(),
                    "amal.simpleParametreModel.codeTraitementDossier.mandatory");
        }
        if (JadeStringUtil.isBlank(parametreModel.getIdFormule())) {
            JadeThread.logError(parametreModel.getClass().getName(), "amal.simpleParametreModel.idFormule.mandatory");
        }
        if (JadeStringUtil.isBlank(parametreModel.getTypeGed())) {
            JadeThread.logError(parametreModel.getClass().getName(), "amal.simpleParametreModel.typeGed.mandatory");
        }
    }

}
