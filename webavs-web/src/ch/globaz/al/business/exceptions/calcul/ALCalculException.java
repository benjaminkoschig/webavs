package ch.globaz.al.business.exceptions.calcul;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour le calcul des droits
 * 
 * @author jts
 */
public class ALCalculException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCalculException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCalculException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCalculException(String m, Throwable t) {
        super(m, t);
    }
}
