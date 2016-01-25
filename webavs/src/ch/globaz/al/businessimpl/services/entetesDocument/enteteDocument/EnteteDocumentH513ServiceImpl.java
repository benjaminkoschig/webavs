/**
 * 
 */
package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH513Service;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * @author pta Implémentation du service qui récupère les entêtes de la caisse H513
 * 
 */
public class EnteteDocumentH513ServiceImpl extends ALAbstractBusinessServiceImpl implements EnteteDocumentH513Service {

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
        return ALConstDocument.H513_ENTETE_STANDARD_FR;
    }

}
