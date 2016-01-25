package ch.globaz.al.business.exceptions.utils;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les classes "utils" des AF
 * 
 * @author jts
 */
public class ALUtilsException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALUtilsException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALUtilsException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALUtilsException(String m, Throwable t) {
        super(m, t);
    }
}
