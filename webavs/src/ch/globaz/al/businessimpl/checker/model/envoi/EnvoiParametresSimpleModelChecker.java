/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de vérification des données de la classe <code>EnvoiParametresSimpleModel</code>
 * 
 * @author dhi
 */
public abstract class EnvoiParametresSimpleModelChecker extends ALAbstractChecker {

    private static void checkBusinessIntegrity(EnvoiParametresSimpleModel envoiParametres) {

        // Recherche si un paramètre de même nature n'existe pas déjà
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(envoiParametres.getCsTypeParametre());
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
            if (searchModel.getSize() > 0) {
                JadeThread.logError(EnvoiParametresSimpleModelChecker.class.getName(),
                        "al.envoi.envoiParametresSimpleModel.csTypeParametre.businessIntegrity");
            }
        } catch (Exception ex) {
            JadeThread.logError(EnvoiParametresSimpleModelChecker.class.getName(),
                    "al.envoi.envoiParametresSimpleModel.csTypeParametre.businessIntegrity.error");
        }
    }

    private static void checkDatabaseIntegrity(EnvoiParametresSimpleModel envoiParametres) {

    }

    private static void checkMandatory(EnvoiParametresSimpleModel envoiParametres) {

        if (JadeStringUtil.isEmpty(envoiParametres.getCsTypeParametre())) {
            JadeThread.logError(EnvoiParametresSimpleModelChecker.class.getName(),
                    "al.envoi.envoiParametresSimpleModel.csTypeParametre.mandatory");
        }

        if (JadeStringUtil.isEmpty(envoiParametres.getValeurParametre())) {
            JadeThread.logError(EnvoiParametresSimpleModelChecker.class.getName(),
                    "al.envoi.envoiParametresSimpleModel.valeurParametre.mandatory");
        }

    }

    public static void validateForCreate(EnvoiParametresSimpleModel envoiParametres) {
        EnvoiParametresSimpleModelChecker.checkMandatory(envoiParametres);
        EnvoiParametresSimpleModelChecker.checkBusinessIntegrity(envoiParametres);
    }

    public static void validateForDelete(EnvoiParametresSimpleModel envoiParametres) {
    }

    public static void validateForUpdate(EnvoiParametresSimpleModel envoiParametres) {
        EnvoiParametresSimpleModelChecker.checkMandatory(envoiParametres);
    }

}
