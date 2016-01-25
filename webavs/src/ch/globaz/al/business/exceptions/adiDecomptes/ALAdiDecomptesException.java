package ch.globaz.al.business.exceptions.adiDecomptes;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> m�tier pour les services business li�s aux d�comptes Adi
 * 
 * @author PTA
 * 
 */
public class ALAdiDecomptesException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAdiDecomptesException() {
        super();
    }

    /**
     * @see ALException#ALException(String m)
     */

    public ALAdiDecomptesException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String m, Throwable t)
     */
    public ALAdiDecomptesException(String m, Throwable t) {
        super(m, t);

    }

}
