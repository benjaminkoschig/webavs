/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.controleurEnvoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class SimpleControleurJobChecker extends AmalAbstractChecker {

    /**
     * Contr�le d'un job � sa cr�ation, les champs doivent �tre renseign�s et correctement si possible :)
     * 
     * @param controleurJob
     */
    public static void checkForCreate(SimpleControleurJob controleurJob) {

        // contr�le que l'ensemble des champs soit renseign�s
        SimpleControleurJobChecker.checkMandatory(controleurJob);
        // contr�le que la valeur des champs soit corrects (type de travaux, status)
        SimpleControleurJobChecker.checkIntegrity(controleurJob);

    }

    /**
     * Contr�le d'un job pour sa suppression, il ne doit pas avoir d'envoi li�s
     * 
     * @param controleurJob
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws ControleurEnvoiException
     */
    public static void checkForDelete(SimpleControleurJob controleurJob)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, ControleurEnvoiException {

        SimpleControleurEnvoiStatusSearch statusSearch = new SimpleControleurEnvoiStatusSearch();
        statusSearch.setForIdJob(controleurJob.getIdJob());
        statusSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(statusSearch);
        if (statusSearch.getSize() > 0) {
            JadeThread.logError(controleurJob.getClass().getName(),
                    "amal.controleurJob.SimpleControleurJob.delete.integrity");
        }
    }

    /**
     * Contr�le d'un job � sa mise � jour, l'ensemble des champs doivent �tre renseign�s correctement
     * 
     * @param controleurJob
     */
    public static void checkForUpdate(SimpleControleurJob controleurJob) {

        // contr�le que l'ensemble des champs soit renseign�s
        SimpleControleurJobChecker.checkMandatory(controleurJob);
        // contr�le que la valeur des champs soit corrects (type de travaux, status)
        SimpleControleurJobChecker.checkIntegrity(controleurJob);

    }

    /**
     * Contr�le que les donn�es de type de job et de status du job soient correctes
     * 
     * @param controleurJob
     */
    private static void checkIntegrity(SimpleControleurJob controleurJob) {

        // contr�le que les types de travaux soient corrects
        if (!JadeStringUtil.isBlankOrZero(controleurJob.getStatusEnvoi())) {
            if (!(controleurJob.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue())
                    || controleurJob.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())
                    || controleurJob.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())
                    || controleurJob.getStatusEnvoi()
                            .equals(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue())
                    || controleurJob.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue()) || controleurJob
                    .getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue()))) {
                JadeThread.logError(controleurJob.getClass().getName(),
                        "amal.controleurJob.SimpleControleurJob.statusEnvoi.integrity");
            }
        }

        // contr�le que les types de jobs soient corrects
        if (!JadeStringUtil.isBlankOrZero(controleurJob.getTypeJob())) {
            if (!(controleurJob.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue())
                    || controleurJob.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())
                    || controleurJob.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue()) || controleurJob
                    .getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue()))) {
                JadeThread.logError(controleurJob.getClass().getName(),
                        "amal.controleurJob.SimpleControleurJob.jobType.integrity");
            }
        }

    }

    /**
     * Contr�le que l'ensemble des champs soient renseign�s
     * 
     * @param controleurJob
     */
    private static void checkMandatory(SimpleControleurJob controleurJob) {

        Boolean bMissed = false;
        if (JadeStringUtil.isBlankOrZero(controleurJob.getDateJob())) {
            bMissed = true;
        }
        if (JadeStringUtil.isBlankOrZero(controleurJob.getDescriptionJob())) {
            bMissed = true;
        }
        if (JadeStringUtil.isBlankOrZero(controleurJob.getStatusEnvoi())) {
            bMissed = true;
        }
        if (JadeStringUtil.isBlankOrZero(controleurJob.getTypeJob())) {
            bMissed = true;
        }
        if (bMissed == true) {
            JadeThread.logError(controleurJob.getClass().getName(),
                    "amal.controleurJob.SimpleControleurJob.allFields.mandatory");
        }

    }

}
