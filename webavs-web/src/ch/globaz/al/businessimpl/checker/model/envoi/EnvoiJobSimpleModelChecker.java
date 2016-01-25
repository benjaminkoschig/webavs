/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.envoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModelSearch;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Classe de vérification des données de la classe <code>EnvoiJobSimpleModel</code>
 * 
 * @author dhi
 */
public class EnvoiJobSimpleModelChecker extends ALAbstractChecker {

    private static void checkBusinessIntegrity(EnvoiJobSimpleModel envoiJob) {
        EnvoiJobSimpleModelSearch searchModel = new EnvoiJobSimpleModelSearch();
        searchModel.setForJobDate(envoiJob.getJobDate());
        searchModel.setForJobStatus(envoiJob.getJobStatus());
        searchModel.setForJobType(envoiJob.getJobType());
        searchModel.setForJobUser(envoiJob.getJobUser());
        try {
            searchModel = ALImplServiceLocator.getEnvoiJobSimpleModelService().search(searchModel);
            if (searchModel.getSize() > 0) {
                JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                        "al.envoi.envoiJobSimpleModel.businessIntegrity");
            }
        } catch (Exception ex) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.businessIntegrity.error");
        }

    }

    private static void checkBusinessIntegrityForDelete(EnvoiJobSimpleModel envoiJob) {

        if (ALCSEnvoi.STATUS_ENVOI_SENT.equals(envoiJob.getJobStatus())) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.businessIntegrityDelete.jobStatus");
        }

    }

    private static void checkDatabaseIntegrity(EnvoiJobSimpleModel envoiJob) {

    }

    private static void checkMandatory(EnvoiJobSimpleModel envoiJob) {

        if (JadeStringUtil.isEmpty(envoiJob.getJobDate())) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.jobDate.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiJob.getJobDescription())) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.jobDescription.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiJob.getJobStatus())) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.jobStatus.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiJob.getJobType())) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.jobType.mandatory");
        }
        if (JadeStringUtil.isEmpty(envoiJob.getJobUser())) {
            JadeThread.logError(EnvoiJobSimpleModelChecker.class.getName(),
                    "al.envoi.envoiJobSimpleModel.jobUser.mandatory");
        }
    }

    public static void validateForCreate(EnvoiJobSimpleModel envoiJob) {
        EnvoiJobSimpleModelChecker.checkMandatory(envoiJob);
        EnvoiJobSimpleModelChecker.checkBusinessIntegrity(envoiJob);
    }

    public static void validateForDelete(EnvoiJobSimpleModel envoiJob) {
        EnvoiJobSimpleModelChecker.checkBusinessIntegrityForDelete(envoiJob);
    }

    public static void validateForUpdate(EnvoiJobSimpleModel envoiJob) {
        EnvoiJobSimpleModelChecker.checkMandatory(envoiJob);
    }

}
