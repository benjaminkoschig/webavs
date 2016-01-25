package ch.globaz.al.business.exceptions.rafam;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour la gestion du RAFam
 * 
 * @author jts
 */
public class ALRafamException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRafamException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRafamException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRafamException(String m, Throwable t) {
        super(m, t);
    }

}