/**
 * 
 */
package ch.globaz.al.business.exceptions.model.allocataire;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier modèle complexe Allocataire Agricole
 * 
 * @author PTA
 */
public class ALAllocataireAgricoleComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAllocataireAgricoleComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAllocataireAgricoleComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAllocataireAgricoleComplexModelException(String m, Throwable t) {
        super(m, t);
    }

}
