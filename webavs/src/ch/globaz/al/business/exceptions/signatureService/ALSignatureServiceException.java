/**
 * 
 */
package ch.globaz.al.business.exceptions.signatureService;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion du service de la gestion des signatures
 * 
 * @author PTA
 * 
 */
public class ALSignatureServiceException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALSignatureServiceException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALSignatureServiceException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALSignatureServiceException(String m, Throwable t) {
        super(m, t);

    }

}
