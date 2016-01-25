package ch.globaz.al.business.exceptions.model.allocataire;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier de l'allocataire
 * 
 * @author jts
 */
public class ALAllocataireModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAllocataireModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAllocataireModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAllocataireModelException(String m, Throwable t) {
        super(m, t);
    }
}