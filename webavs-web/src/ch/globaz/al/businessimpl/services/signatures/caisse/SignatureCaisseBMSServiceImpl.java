/**
 * 
 */
package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseBMSService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service de signature du BMS
 * 
 * @author jts
 * 
 */
public class SignatureCaisseBMSServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseBMSService {

    @Override
    public String getLibelleSignature(String activiteAllocataire, String typeDocument) throws JadeApplicationException,
            JadePersistenceException {
        return ALConstDocument.SIGNATURE_CAISSE_BMS;
    }
}
