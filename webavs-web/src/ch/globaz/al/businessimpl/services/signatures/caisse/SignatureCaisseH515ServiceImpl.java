/**
 * 
 */
package ch.globaz.al.businessimpl.services.signatures.caisse;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.services.signatures.caisse.SignatureCaisseH515Service;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * @author PTA
 * 
 */
public class SignatureCaisseH515ServiceImpl extends ALAbstractBusinessServiceImpl implements SignatureCaisseH515Service {

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
        return ALConstDocument.SIGNATURE_CAISSE_H515;
    }

}
