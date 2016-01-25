/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.document;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.services.models.document.SimpleDocumentService;
import ch.globaz.amal.businessimpl.checkers.document.SimpleDocumentChecker;

/**
 * @author DHI
 * 
 */
public class SimpleDocumentServiceImpl implements SimpleDocumentService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.document.SimpleDocumentService#create(ch.globaz.amal.business.models.
     * documents.SimpleDocument)
     */
    @Override
    public SimpleDocument create(SimpleDocument simpleDocument) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DocumentException, DetailFamilleException,
            ControleurEnvoiException, AnnonceException, ControleurJobException {
        return this.create(simpleDocument, "");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.document.SimpleDocumentService#create(ch.globaz.amal.business.models.
     * documents.SimpleDocument)
     */
    @Override
    public SimpleDocument create(SimpleDocument simpleDocument, String csJobType) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DocumentException, DetailFamilleException,
            ControleurEnvoiException, AnnonceException, ControleurJobException {
        if (simpleDocument == null) {
            throw new DocumentException("Unable to create a simple document, the model passed is null");
        }
        SimpleDocumentChecker.checkForCreate(simpleDocument);
        SimpleDocument documentToAdd = null;
        documentToAdd = (SimpleDocument) JadePersistenceManager.add(simpleDocument);
        return documentToAdd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.document.SimpleDocumentService#delete(ch.globaz.amal.business.models.
     * documents.SimpleDocument)
     */
    @Override
    public SimpleDocument delete(SimpleDocument simpleDocument) throws JadePersistenceException, DocumentException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException {
        if (simpleDocument == null) {
            throw new DocumentException("Unable to delete the simple document, the model passed is null");
        }
        SimpleDocumentChecker.checkForDelete(simpleDocument);
        return (SimpleDocument) JadePersistenceManager.delete(simpleDocument);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.document.SimpleDocumentService#read(java.lang.String)
     */
    @Override
    public SimpleDocument read(String idDocument) throws JadePersistenceException, DocumentException {
        if (JadeStringUtil.isBlank(idDocument)) {
            throw new DocumentException("Unable to read the document, the id passed is empty");
        }
        SimpleDocument simpleDocument = new SimpleDocument();
        simpleDocument.setId(idDocument);
        return (SimpleDocument) JadePersistenceManager.read(simpleDocument);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.document.SimpleDocumentService#search(ch.globaz.amal.business.models.
     * documents.SimpleDocumentSearch)
     */
    @Override
    public SimpleDocumentSearch search(SimpleDocumentSearch search) throws JadePersistenceException, DocumentException {
        if (search == null) {
            throw new DocumentException("Unable to search a simple document, the search model passed is null");
        }
        return (SimpleDocumentSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.document.SimpleDocumentService#update(ch.globaz.amal.business.models.
     * documents.SimpleDocument)
     */
    @Override
    public SimpleDocument update(SimpleDocument simpleDocument) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, DocumentException, DetailFamilleException {
        if (simpleDocument == null) {
            throw new DocumentException("Unable to update the simple document, the model passed is null");
        }
        SimpleDocumentChecker.checkForUpdate(simpleDocument);
        return (SimpleDocument) JadePersistenceManager.update(simpleDocument);
    }

}
