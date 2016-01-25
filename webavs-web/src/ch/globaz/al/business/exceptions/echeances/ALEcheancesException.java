package ch.globaz.al.business.exceptions.echeances;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Exception lié aux avis d'échéances
 * 
 * @author gmo
 * 
 */
public class ALEcheancesException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEcheancesException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEcheancesException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEcheancesException(String m, Throwable t) {
        super(m, t);

    }
}
