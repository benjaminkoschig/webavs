package ch.globaz.al.business.exceptions.model.allocataire;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier du revenu
 * 
 * @author jts
 */
public class ALRevenuModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRevenuModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRevenuModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRevenuModelException(String m, Throwable t) {
        super(m, t);
    }
}