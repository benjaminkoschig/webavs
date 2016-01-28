package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Classe parente de tous les implémentations du service de récupération d'en-tête de document
 * 
 * @author jts
 * 
 */
public class AbstractEnteteDocumentServiceImpl extends ALAbstractBusinessServiceImpl {

    /**
     * Vérifie que le type de document soit valide
     * 
     * @param typeDocument
     *            type de document à vérifier ({@link ch.globaz.al.business.constantes.ALConstDocument})
     * @return <code>true</code> si le type est valide, <code>false</code> sinon
     */
    protected boolean typeDocumentIsValid(String typeDocument) {
        return !(!JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECISION, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_DIR, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_ECHEANCE_ALLOC_IND, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.LETTRE_ACCOMPAGNEMENT, false)
                && !JadeStringUtil.equals(typeDocument, ALConstDocument.DOCUMENT_PROTOCOLE, false) && !JadeStringUtil
                    .equals(typeDocument, ALConstDocument.RECAPITULATIF_DOCUMENT, false));
    }
}
