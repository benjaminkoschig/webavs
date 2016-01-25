/**
 * 
 */
package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentFVEService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de la gestion des entêtes de document de la FVE
 * 
 * @author pta
 * 
 */
public class EnteteDocumentFVEServiceImpl extends ALAbstractBusinessServiceImpl implements EnteteDocumentFVEService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentService#getEnteteDocument(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {
        // une seule entete pour la H513
        return ALConstDocument.FVE_ENTETE_STANDARD;
    }

}
