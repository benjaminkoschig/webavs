package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH51XService;

/**
 * Implémentation du service récupérant les entêtes de la H51X
 * 
 * @author PTA
 * 
 */
public class EnteteDocumentH51XServiceImpl extends AbstractEnteteDocumentServiceImpl implements
        EnteteDocumentH51XService {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {

        // pas de vérification de l'activité de l'allocataire et du type de document, pas utilisés ici

        if (!typeDocumentIsValid(typeDocument)) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentH51XServiceImpl#getEnteteDocument typeDocument is not valid");
        }

        if (JadeStringUtil.isBlank(langue)) {
            throw new ALEnteteDocumentException("EnteteDocumentH51XServiceImpl#getEnteteDocument: langue undefined");
        }

        if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.H51X_ENTETE_DECISION_DE;

        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.H51X_ENTETE_DECISION_FR;
            
        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ITALIEN, false)
                && JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.H51X_ENTETE_DECISION_IT;            

        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.H51X_ENTETE_STANDARD_FR;

        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.H51X_ENTETE_STANDARD_DE;
            
        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ITALIEN, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)) {
            return ALConstDocument.H51X_ENTETE_STANDARD_IT;            

        } else {
            return ALConstDocument.H51X_ENTETE_STANDARD_FR;
        }
    }

}
