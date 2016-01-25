package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentAGLSService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

public class EnteteDocumentAGLSServiceImpl extends ALAbstractBusinessServiceImpl implements EnteteDocumentAGLSService {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.AGLS_ENTETE_DECISION;
        } else {
            return ALConstDocument.AGLS_ENTETE_STANDARD;
        }
    }

}
