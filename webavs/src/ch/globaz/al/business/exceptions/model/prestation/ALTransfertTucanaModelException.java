package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * classe <code>Exception</code> métier de TransfertTucana
 * 
 * @author PTA
 */

public class ALTransfertTucanaModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALTransfertTucanaModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALTransfertTucanaModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALTransfertTucanaModelException(String m, Throwable t) {
        super(m, t);
    }
}
