/**
 * 
 */
package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseFVEService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * @author pta
 * 
 */
public class SignatureCaisseFVEServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseFVEService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.signatures.caisse.SignatureCaisseService#getLibelleSignature(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getLibelleSignature(String activiteAllocataire, String typeDocument) throws JadeApplicationException,
            JadePersistenceException {

        return ALConstDocument.SIGNATURE_CAISSE_FVE;
    }

}
