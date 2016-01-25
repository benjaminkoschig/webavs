/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModelSearch;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de vérification des données de la classe <code>EnvoiTemplateSimpleModel</code>
 * 
 * @author dhi
 */
public abstract class EnvoiTemplateSimpleModelChecker extends ALAbstractChecker {

    private static void checkBusinessIntegrity(EnvoiTemplateSimpleModel envoiTemplate) {

        EnvoiTemplateSimpleModelSearch searchModel = new EnvoiTemplateSimpleModelSearch();
        searchModel.setForIdFormule(envoiTemplate.getIdFormule());
        try {
            searchModel = ALImplServiceLocator.getEnvoiTemplateSimpleModelService().search(searchModel);
            if (searchModel.getSize() > 0) {
                JadeThread.logError(EnvoiTemplateSimpleModelChecker.class.getName(),
                        "al.envoi.envoiTemplateSimpleModel.idFormule.businessIntegrity");
            }
        } catch (Exception ex) {
            JadeThread.logError(EnvoiTemplateSimpleModelChecker.class.getName(),
                    "al.envoi.envoiTemplateSimpleModel.idFormule.businessIntegrity.error");
        }

    }

    private static void checkDatabaseIntegrity(EnvoiTemplateSimpleModel envoiTemplate) {

    }

    private static void checkMandatory(EnvoiTemplateSimpleModel envoiTemplate) {

        if (JadeStringUtil.isEmpty(envoiTemplate.getIdFormule())) {
            JadeThread.logError(EnvoiTemplateSimpleModelChecker.class.getName(),
                    "al.envoi.envoiTemplateSimpleModel.idFormule.mandatory");
        }

    }

    public static void validateForCreate(EnvoiTemplateSimpleModel envoiTemplate) {
        EnvoiTemplateSimpleModelChecker.checkMandatory(envoiTemplate);
        EnvoiTemplateSimpleModelChecker.checkBusinessIntegrity(envoiTemplate);
    }

    public static void validateForDelete(EnvoiTemplateSimpleModel envoiTemplate) {
        JadeThread
                .logError(EnvoiTemplateSimpleModelChecker.class.getName(), "al.envoi.envoiTemplateSimpleModel.delete");
    }

    public static void validateForUpdate(EnvoiTemplateSimpleModel envoiTemplate) {
        EnvoiTemplateSimpleModelChecker.checkMandatory(envoiTemplate);
    }

}
