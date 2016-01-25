package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH510Service;

/**
 * Implémentation du service de la gestion des entêtes de document de la H51.0
 * 
 * @author pta
 * 
 */
public class EnteteDocumentH510ServiceImpl extends AbstractEnteteDocumentServiceImpl implements
        EnteteDocumentH510Service {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {

        // Pas de vérification de activiteAllocataire et typeDocument, ne sont pas utilisés dans le cas présent

        if (JadeStringUtil.isBlank(langue)) {
            throw new ALEnteteDocumentException("EnteteDocumentH510ServiceImpl#getEnteteDocument: langue undefined");
        }

        if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_FRANCAIS, false)) {
            return ALConstDocument.H510_ENTETE_STANDARD_FR;

        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ALLEMAND, false)) {
            return ALConstDocument.H510_ENTETE_STANDARD_DE;

        } else {
            return ALConstDocument.H510_ENTETE_STANDARD_FR;
        }
    }

}
