package ch.globaz.al.business.exceptions.prestations;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la compensation de prestations
 * 
 * @author jts
 */
public class ALCompensationPrestationException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCompensationPrestationException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCompensationPrestationException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCompensationPrestationException(String m, Throwable t) {
        super(m, t);
    }
}
