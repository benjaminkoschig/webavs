package ch.globaz.al.business.exceptions.rafam;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour la gestion du RAFam
 * 
 * @author jts
 */
public class ALRafamEnumException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRafamEnumException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRafamEnumException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRafamEnumException(String m, Throwable t) {
        super(m, t);
    }

}