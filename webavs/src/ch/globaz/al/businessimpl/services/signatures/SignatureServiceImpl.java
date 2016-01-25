/**
 * 
 */
package ch.globaz.al.businessimpl.services.signatures;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.services.signatures.SignatureService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.signatures.caisse.SignatureCaisseFactory;

/**
 * @author PTA
 * 
 */
public class SignatureServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.signatures.SignatureService#getLibelleSignature(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public String getLibelleSignature(/* String nomCaisse, */String activiteAllocataire, String typeDocument)
            throws JadeApplicationException, JadePersistenceException {
        // Class<?> classSignature = SignatureCaisseFactory.getServiceSignatureCaisse();
        // SignatureService service = (SignatureService) ALImplServiceLocator.getSignatureCaisseService(classSignature);
        // service.getLibelleSignature(/* nomCaisse, */activiteAllocataire, typeDocument);
        return (ALImplServiceLocator.getSignatureCaisseService(SignatureCaisseFactory.getServiceSignatureCaisse()))
                .getLibelleSignature(/* nomCaisse, */activiteAllocataire, typeDocument);
    }

}
