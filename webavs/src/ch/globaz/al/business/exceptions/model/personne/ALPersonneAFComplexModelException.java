/**
 * 
 */
package ch.globaz.al.business.exceptions.model.personne;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier modèle complexe PersonneAF
 * 
 * @author PTA
 * 
 */
public class ALPersonneAFComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALPersonneAFComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALPersonneAFComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALPersonneAFComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}
