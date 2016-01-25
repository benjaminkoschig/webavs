/**
 * 
 */
package ch.globaz.al.business.exceptions.model.adi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier ADI - modèle Décompte
 * 
 * @author PTA
 */
public class ALDecompteAdiModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDecompteAdiModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDecompteAdiModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDecompteAdiModelException(String m, Throwable t) {
        super(m, t);
    }
}
