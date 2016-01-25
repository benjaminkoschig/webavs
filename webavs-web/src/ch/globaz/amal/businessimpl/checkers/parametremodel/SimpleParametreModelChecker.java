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
     * Validation des paramètres de modèle de document lors de sa création Les champs doivent être renseignés Des
     * paramètres n'existent pas encore pour le même id formule
     * 
     * @param parametreModel
     */
    public static void checkForCreate(SimpleParametreModel parametreModel) {
        // Contrôle que les champs soient renseignés
        SimpleParametreModelChecker.checkMandatory(parametreModel);
        // Contrôle qu'un paramètre n'est pas déjà existant + dates début/fin
        SimpleParametreModelChecker.checkIntegrity(parametreModel);
    }

    /**
     * Validation des paramètres d'un modèle de document lors de sa suppression
     * 
     * PAS DE CONTRÔLE PARTICULIER
     * 
     * @param parametreModel
     */
    public static void checkForDelete(SimpleParametreModel parametreModel) {
        // Pas de contrôle particulier
    }

    /**
     * Validation des paramètres de modèle de document lors de sa mise à jour Les champs doivent être renseignés Des
     * paramètres n'existent pas encore pour le même id formule
     * 
     * @param parametreModel
     */
    public static void checkForUpdate(SimpleParametreModel parametreModel) {
        // Contrôle que les champs soient renseignés
        SimpleParametreModelChecker.checkMandatory(parametreModel);
        // Contrôle qu'un paramètre n'est pas déjà existant + dates début/fin
        SimpleParametreModelChecker.checkIntegrity(parametreModel);
    }

    /**
     * Contrôle les dates de validités du model et contrôle qu'un autre paramètre n'existe pas avec un même id formule
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
     * contrôle que l'ensemble des champs soit renseigné
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
