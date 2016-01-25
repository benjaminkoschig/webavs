package ch.globaz.al.business.exceptions.ged;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les services GED
 * 
 * @author jts
 * 
 */
public class ALGedException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALGedException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALGedException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALGedException(String m, Throwable t) {
        super(m, t);

    }

}
