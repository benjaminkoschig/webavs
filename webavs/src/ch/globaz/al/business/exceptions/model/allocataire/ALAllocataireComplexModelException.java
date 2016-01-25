/**
 * 
 */
package ch.globaz.al.business.exceptions.model.allocataire;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier modèle complexe Allocataire
 * 
 * @author PTA
 * 
 */
public class ALAllocataireComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAllocataireComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAllocataireComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAllocataireComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}
