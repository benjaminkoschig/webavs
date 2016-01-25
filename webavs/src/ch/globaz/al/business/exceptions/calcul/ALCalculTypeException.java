package ch.globaz.al.business.exceptions.calcul;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les types de calcul
 * 
 * @author jts
 */
public class ALCalculTypeException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCalculTypeException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCalculTypeException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCalculTypeException(String m, Throwable t) {
        super(m, t);
    }
}