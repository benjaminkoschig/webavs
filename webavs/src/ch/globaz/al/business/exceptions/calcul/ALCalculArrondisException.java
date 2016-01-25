package ch.globaz.al.business.exceptions.calcul;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les calculs d'arrondis
 * 
 * @author jts
 */
public class ALCalculArrondisException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCalculArrondisException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCalculArrondisException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCalculArrondisException(String m, Throwable t) {
        super(m, t);
    }
}
