package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentCICIService;

/**
 * Impl�mentation du service de la gestion des ent�tes de document de la cicicam
 * 
 * @author PTA
 * 
 */
public class EnteteDocumentCICIServiceImpl extends AbstractEnteteDocumentServiceImpl implements
        EnteteDocumentCICIService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentService#getEnteteDocument(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langueAffilie)
            throws JadeApplicationException, JadePersistenceException {

        // pas de v�rification des param�tres, toujours le cas standard

        return ALConstDocument.CICICAM_STANDARD;
    }

}
