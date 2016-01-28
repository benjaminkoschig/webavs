package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentCCVDService;

/**
 * Implémentation du service qui récupère les entêtes de la CCVD
 * 
 * @author pta
 * 
 */
public class EnteteDocumentCCVDServiceImpl extends AbstractEnteteDocumentServiceImpl implements
        EnteteDocumentCCVDService {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {

        // pas de vérification de la langue, pas utilisé ici

        try {
            if (!ALConstDocument.DOCUMENT_PROTOCOLE.equals(typeDocument)
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC, activiteAllocataire)) {
                throw new ALEnteteDocumentException(
                        "EnteteDocumentCCVDServiceImpl#getEnteteDocument: activiteAllocataire is not valid");
            }
        } catch (Exception e) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentCCVDServiceImpl#getEnteteDocument: exception thrown during check (activiteAllocataire) "
                            + e.getMessage(), e);
        }

        if (!typeDocumentIsValid(typeDocument)) {
            throw new ALEnteteDocumentException(
                    "EnteteDocumentCCVDServiceImpl#getEnteteDocument typeDocument is not valid");
        }

        // si allocataire agricole et type de document decision entete ccvd caisseCompensation
        if ((JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_AGRICULTEUR, false)
                || JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, false)
                || JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false) || JadeStringUtil
                    .equals(activiteAllocataire, ALCSDossier.ACTIVITE_PECHEUR, false))
                && (JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                        || JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                        || JadeStringUtil.equals(typeDocument, ALConstDocument.RECAPITULATIF_DOCUMENT, false)
                        || JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false) || JadeStringUtil
                            .equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false))) {
            return ALConstDocument.CCVD_ENTETE_CAISSE_COMPENS;
        }

        // autre cas entete ccvd allocation familiale

        else {
            return ALConstDocument.CCVD_ENTETE_CAISSE_AF;
        }
    }

}
