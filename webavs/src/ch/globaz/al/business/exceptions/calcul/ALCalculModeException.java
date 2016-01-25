package ch.globaz.al.business.exceptions.calcul;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les modes de calcul
 * 
 * @author jts
 */
public class ALCalculModeException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCalculModeException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCalculModeException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCalculModeException(String m, Throwable t) {
        super(m, t);
    }
}