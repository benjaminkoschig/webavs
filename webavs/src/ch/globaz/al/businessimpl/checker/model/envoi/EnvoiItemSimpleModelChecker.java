/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * Classe de vérification des données de la classe <code>EnvoiItemSimpleModel</code>
 * 
 * @author dhi
 */
public abstract class EnvoiItemSimpleModelChecker extends ALAbstractChecker {

    private static void checkBusinessIntegrity(EnvoiItemSimpleModel envoiItem) {

    }

    private static void checkBusinessIntegrityForDelete(EnvoiItemSimpleModel envoiItem) {

        if (ALCSEnvoi.STATUS_ENVOI_SENT.equals(envoiItem.getEnvoiStatus())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.envoiStatus.businessIntegrityDelete");
        }

    }

    private static void checkDatabaseIntegrity(EnvoiItemSimpleModel envoiItem) {

    }

    private static void checkMandatory(EnvoiItemSimpleModel envoiItem) {
        if (JadeStringUtil.isEmpty(envoiItem.getEnvoiFileName())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.fileName.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiItem.getEnvoiStatus())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.envoiStatus.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiItem.getEnvoiType())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.envoiType.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiItem.getEnvoiNoGroupe())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.envoiNoGroupe.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiItem.getIdExternalLink())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.idExternalLink.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiItem.getIdFormule())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.idFormule.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiItem.getIdJob())) {
            JadeThread.logError(EnvoiItemSimpleModelChecker.class.getName(),
                    "al.envoi.envoiItemSimpleModel.idJob.mandatory");
        }
    }

    public static void validateForCreate(EnvoiItemSimpleModel envoiItem) {
        EnvoiItemSimpleModelChecker.checkMandatory(envoiItem);
        EnvoiItemSimpleModelChecker.checkBusinessIntegrity(envoiItem);
    }

    public static void validateForDelete(EnvoiItemSimpleModel envoiItem) {
        EnvoiItemSimpleModelChecker.checkBusinessIntegrityForDelete(envoiItem);
    }

    public static void validateForUpdate(EnvoiItemSimpleModel envoiItem) {
        EnvoiItemSimpleModelChecker.checkMandatory(envoiItem);
        EnvoiItemSimpleModelChecker.checkBusinessIntegrity(envoiItem);
    }

}
