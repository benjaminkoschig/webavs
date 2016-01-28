/**
 * 
 */
package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseFPVService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * Implémentation du service des signatures de la FPV
 * 
 * @author pta
 * 
 */
public class SignatureCaisseFPVServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseFPVService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.signatures.SignatureService#getLibelleSignature(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getLibelleSignature(String activiteAllocataire, String typeDocument) throws JadeApplicationException,
            JadePersistenceException {
        return ALConstDocument.SIGNATURE_CAISSE_FPV;
    }

}
