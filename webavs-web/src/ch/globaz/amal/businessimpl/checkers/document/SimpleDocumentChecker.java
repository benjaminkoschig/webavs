/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.document;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class SimpleDocumentChecker extends AmalAbstractChecker {

    /**
     * Contr�le pour la cr�ation d'un document, les champs doivent �tre renseign� et le document li� � un subside
     * 
     * @param document
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleDocument document) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les champs doivent tous �tre renseign�s (sauf libell�)
        // Le lien avec detail famille doit exister
        SimpleDocumentChecker.checkMandatory(document);
        SimpleDocumentChecker.checkIntegrity(document);

    }

    /**
     * Contr�le pour la suppression d'un document, le document ne doit pas avoir un status sent
     * 
     * @param document
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws ControleurEnvoiException
     */
    public static void checkForDelete(SimpleDocument document) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, ControleurEnvoiException {

        // Effacement autoris� si le document n'a pas de status sent
        SimpleControleurEnvoiStatusSearch envoiSearch = new SimpleControleurEnvoiStatusSearch();
        envoiSearch.setForIdEnvoi(document.getIdDetailEnvoiDocument());
        envoiSearch = AmalImplServiceLocator.getSimpleControleurEnvoiStatusService().search(envoiSearch);
        for (int iEnvoi = 0; iEnvoi < envoiSearch.getSize(); iEnvoi++) {
            SimpleControleurEnvoiStatus envoi = (SimpleControleurEnvoiStatus) envoiSearch.getSearchResults()[iEnvoi];
            if (envoi.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                JadeThread
                        .logError(document.getClass().getName(), "amal.document.simpleDocument.statusEnvoi.integrity");
                break;
            }
        }

    }

    /**
     * Contr�le pour la mise � jour d'un document, les champs doivent �tre renseign� et le document li� � un subside
     * 
     * @param document
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimpleDocument document) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les champs doivent tous �tre renseign�s (sauf libell�)
        // Le lien avec detail famille doit exister
        SimpleDocumentChecker.checkMandatory(document);
        SimpleDocumentChecker.checkIntegrity(document);
    }

    /**
     * Contr�le que nous trouvions le subside associ� au document
     * 
     * @param document
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimpleDocument document) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        SimpleDetailFamilleSearch detailSearch = new SimpleDetailFamilleSearch();
        detailSearch.setForIdDetailFamille(document.getIdDetailFamille());
        detailSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(detailSearch);
        if (detailSearch.getSize() != 1) {
            JadeThread
                    .logError(document.getClass().getName(), "amal.document.simpleDocument.idDetailFamille.integrity");
        }
    }

    /**
     * Contr�le la pr�sence des champs du document
     * 
     * @param document
     */
    private static void checkMandatory(SimpleDocument document) {
        Boolean bMissed = false;
        if (JadeStringUtil.isBlankOrZero(document.getDateEnvoi())) {
            bMissed = true;
        }
        if (JadeStringUtil.isBlankOrZero(document.getIdDetailFamille())) {
            bMissed = true;
        }
        if (JadeStringUtil.isBlankOrZero(document.getNumModele())) {
            bMissed = true;
        }
        if (bMissed) {
            JadeThread.logError(document.getClass().getName(), "amal.document.simpleDocument.allFields.mandatory");
        }

    }

}
