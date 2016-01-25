package ch.globaz.al.businessimpl.services.entetesDocument;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.EnteteService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument.EnteteDocumentFactory;

public class EnteteServiceImpl extends ALAbstractBusinessServiceImpl implements EnteteService {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langueAffilie)
            throws JadeApplicationException, JadePersistenceException {

        if (!JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.LETTRE_ACCOMPAGNEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_PROTOCOLE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.RECAPITULATIF_DOCUMENT, false)) {
            throw new ALEnteteDocumentException("EnteteServiceImpl#getEnteteDocument typeDocument is not valid");
        }

        return (ALImplServiceLocator.getEnteteDocumentService(EnteteDocumentFactory.getServiceEnteteDocument()))
                .getEnteteDocument(activiteAllocataire, typeDocument, langueAffilie);
    }

}
