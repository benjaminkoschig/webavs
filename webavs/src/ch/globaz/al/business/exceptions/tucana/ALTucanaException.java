package ch.globaz.al.business.exceptions.tucana;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion des rubriques
 * 
 * @author jts
 * 
 */
public class ALTucanaException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALTucanaException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALTucanaException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALTucanaException(String m, Throwable t) {
        super(m, t);

    }
}