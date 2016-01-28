package ch.globaz.al.business.exceptions.model.allocataire;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des allocataires agriculteur
 * 
 * @author jts
 */
public class ALAgricoleModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAgricoleModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAgricoleModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAgricoleModelException(String m, Throwable t) {
        super(m, t);
    }
}