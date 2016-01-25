package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentCCJUService;

/**
 * Implémentation du service récupérant les entêtes de la CCJU
 * 
 * @author PTA
 * 
 */
public class EnteteDocumentCCJUServiceImpl extends AbstractEnteteDocumentServiceImpl implements
        EnteteDocumentCCJUService {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {

        // pas de vérification de la langue, pas utilisé ici

        if (!typeDocumentIsValid(typeDocument)) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentCCJUServiceImpl#getEnteteDocument typeDocument is not valid");
        }

        try {
            if (!ALConstDocument.DOCUMENT_PROTOCOLE.equals(typeDocument)
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire)) {
                throw new ALEnteteDocumentException(
                        "EnteteDocumentCCJUServiceImpl#getEnteteDocument: activiteAllocataire is not valid");
            }
        } catch (Exception e) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentCCJUServiceImpl#getEnteteDocument: exception thrown during check (activiteAllocataire) "
                            + e.getMessage(), e);
        }

        // si allocataire agricole et type de document decision entete ccju caisseCompensation
        if ((JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_AGRICULTEUR, false)
                || JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, false) || JadeStringUtil
                    .equals(activiteAllocataire, ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false))
                && (JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                        || JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                        || JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false) || JadeStringUtil
                            .equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false))) {

            return ALConstDocument.CCJU_ENTETE_CAISSE_COMPENS;
        }

        // si type de document protocole entete cccju
        else if (JadeStringUtil.equals(ALConstDocument.DOCUMENT_PROTOCOLE, typeDocument, false)) {
            return ALConstDocument.CCJU_ENTETE_PROTOCOLE_AF;
        }

        // autre cas entete ccju allocation familiale
        else {
            return ALConstDocument.CCJU_ENTETE_CAISSE_AF;
        }
    }
}
