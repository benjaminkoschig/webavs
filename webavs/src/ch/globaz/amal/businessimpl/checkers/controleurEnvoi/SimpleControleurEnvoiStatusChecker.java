/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.controleurEnvoi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class SimpleControleurEnvoiStatusChecker extends AmalAbstractChecker {

    /**
     * Controle d'un envoi pour sa cr�ation
     * 
     * @param controleurEnvoi
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AnnonceException
     * @throws DocumentException
     * @throws ControleurJobException
     */
    public static void checkForCreate(SimpleControleurEnvoiStatus controleurEnvoi)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceException,
            DocumentException, ControleurJobException {

        // contr�le si un id document ou id annonce est pr�sente et existante
        SimpleControleurEnvoiStatusChecker.checkMandatory(controleurEnvoi);
        SimpleControleurEnvoiStatusChecker.checkIntegrity(controleurEnvoi);
    }

    /**
     * Contr�le d'un envoi avant effacement
     * 
     * @param controleurEnvoi
     */
    public static void checkForDelete(SimpleControleurEnvoiStatus controleurEnvoi) {

        // contr�le si le status n'est pas � envoy�
        if (controleurEnvoi.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
            JadeThread.logError(controleurEnvoi.getClass().getName(),
                    "amal.controleurEnvoi.SimpleControleurEnvoiStatus.statusEnvoi.sent.integrity");
        }

    }

    /**
     * Contr�le d'un envoi pour sa mise � jour
     * 
     * @param controleurEnvoi
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AnnonceException
     * @throws DocumentException
     * @throws ControleurJobException
     */
    public static void checkForUpdate(SimpleControleurEnvoiStatus controleurEnvoi)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceException,
            DocumentException, ControleurJobException {

        // contr�le si l'id de document/annonce est pr�sent et existant
        SimpleControleurEnvoiStatusChecker.checkMandatory(controleurEnvoi);
        SimpleControleurEnvoiStatusChecker.checkIntegrity(controleurEnvoi);

    }

    /**
     * Contr�le si les envois sont li�s � un job et � un document/annonce
     * 
     * @param controleurEnvoi
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AnnonceException
     * @throws DocumentException
     * @throws ControleurJobException
     */
    private static void checkIntegrity(SimpleControleurEnvoiStatus controleurEnvoi)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, AnnonceException,
            DocumentException, ControleurJobException {

        // Contr�le du type de document et de l'id utilis�
        if (!JadeStringUtil.isEmpty(controleurEnvoi.getTypeEnvoi())) {
            if (controleurEnvoi.getTypeEnvoi().equals(IAMCodeSysteme.AMDocumentType.ENVOI.getValue())) {
                if (!JadeStringUtil.isEmpty(controleurEnvoi.getIdEnvoi())) {
                    SimpleDocumentSearch documentSearch = new SimpleDocumentSearch();
                    documentSearch.setForIdDocument(controleurEnvoi.getIdEnvoi());
                    documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
                    if (documentSearch.getSize() != 1) {
                        JadeThread.logError(controleurEnvoi.getClass().getName(),
                                "amal.controleurEnvoi.SimpleControleurEnvoiStatus.idEnvoi.integrity");
                    }
                }

            } else if (controleurEnvoi.getTypeEnvoi().equals(IAMCodeSysteme.AMDocumentType.ANNONCE.getValue())) {
                if (!JadeStringUtil.isEmpty(controleurEnvoi.getIdAnnonce())) {
                    SimpleAnnonceSearch annonceSearch = new SimpleAnnonceSearch();
                    annonceSearch.setForIdDetailAnnonce(controleurEnvoi.getIdAnnonce());
                    annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
                    if (annonceSearch.getSize() != 1) {
                        JadeThread.logError(controleurEnvoi.getClass().getName(),
                                "amal.controleurEnvoi.SimpleControleurEnvoiStatus.idAnnonce.integrity");
                    }
                }
            } else {
                JadeThread.logError(controleurEnvoi.getClass().getName(),
                        "amal.controleurEnvoi.SimpleControleurEnvoiStatus.typeEnvoi.integrity");
            }
        }

        // Contr�le que le job est pr�sent
        if (!JadeStringUtil.isEmpty(controleurEnvoi.getIdJob())) {
            SimpleControleurJobSearch controleurJobSearch = new SimpleControleurJobSearch();
            controleurJobSearch.setForIdJob(controleurEnvoi.getIdJob());
            controleurJobSearch = AmalImplServiceLocator.getSimpleControleurJobService().search(controleurJobSearch);

            if (controleurJobSearch.getSize() != 1) {
                JadeThread.logError(controleurEnvoi.getClass().getName(),
                        "amal.controleurEnvoi.SimpleControleurEnvoiStatus.idJob.integrity");
            }
        }

    }

    /**
     * Contr�le des champs devant �tre renseign�
     * 
     * @param controleurEnvoi
     */
    private static void checkMandatory(SimpleControleurEnvoiStatus controleurEnvoi) {

        // Contr�le de la pr�sence des iddocument/idannonce
        if (JadeStringUtil.isBlank(controleurEnvoi.getIdAnnonce())
                || JadeStringUtil.isBlank(controleurEnvoi.getIdEnvoi())) {
            JadeThread.logError(controleurEnvoi.getClass().getName(),
                    "amal.controleurEnvoi.SimpleControleurEnvoiStatus.idDocumentAnnonce.mandatory");
        }
        // Contr�le de la pr�sence d'un id job
        if (JadeStringUtil.isBlank(controleurEnvoi.getIdJob())) {
            JadeThread.logError(controleurEnvoi.getClass().getName(),
                    "amal.controleurEnvoi.SimpleControleurEnvoiStatus.idJob.mandatory");
        }
    }

}
