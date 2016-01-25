package ch.globaz.al.businessimpl.services.entetesDocument.enteteDocument;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.enteteDocument.ALEnteteDocumentException;
import ch.globaz.al.business.services.entetesDocument.enteteDocument.EnteteDocumentH515Service;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de la gestion des entêtes de document de la H51.5
 * 
 * @author pta
 * 
 */
public class EnteteDocumentH515ServiceImpl extends ALAbstractBusinessServiceImpl implements EnteteDocumentH515Service {

    @Override
    public String getEnteteDocument(String activiteAllocataire, String typeDocument, String langue)
            throws JadeApplicationException, JadePersistenceException {

        // pas de vérification de l'activité de l'allocataire et du type de document, pas utilisés ici

        if (JadeStringUtil.isBlank(langue)) {
            throw new ALEnteteDocumentException("EnteteDocumentH515ServiceImpl#getEnteteDocument: langue undefined");
        }

        if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_FRANCAIS, false)) {
            return ALConstDocument.H515_ENTETE_STANDARD_FR;

        } else if (JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ALLEMAND, false)) {
            return ALConstDocument.H515_ENTETE_STANDARD_DE;

        }

        else {
            return ALConstDocument.H515_ENTETE_STANDARD_FR;
        }
    }

}
