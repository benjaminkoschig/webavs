package ch.globaz.al.business.exceptions.protocoles;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour l'impression de protocoles
 * 
 * @author jts
 */
public class ALProtocoleException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALProtocoleException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALProtocoleException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALProtocoleException(String m, Throwable t) {
        super(m, t);
    }
}