package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentService;

/**
 * Implémentation standard du service permettant de récupérer l'entête d'une caisse d'une caisse destinée à une template
 * 
 * @author PTA
 * 
 */
public class EnteteDocumentServiceImpl extends AbstractEnteteDocumentServiceImpl implements EnteteDocumentService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentService#getEnteteDocument(java.lang
     * .String, java.lang.String)
     */
    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {

        // pas de vérification de la langue, pas utilisé ici

        try {
            if (!ALConstDocument.DOCUMENT_PROTOCOLE.equals(typeDocument)
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire)) {
                throw new ALEnteteDocumentException(
                        "EnteteDocumentServiceImpl#getEnteteDocument: activiteAllocataire is not valid");
            }
        } catch (Exception e) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentServiceImpl#getEnteteDocument: exception thrown during check (activiteAllocataire) "
                            + e.getMessage(), e);
        }

        if (!typeDocumentIsValid(typeDocument)) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentCCJUServiceImpl#getEnteteDocument typeDocument is not valid");
        }

        if (JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_PROTOCOLE, false)) {
            return "Commun_Protocole_CaisseAF";
        } else {
            return "Commun_Document_CaisseAF";
        }
    }

}
