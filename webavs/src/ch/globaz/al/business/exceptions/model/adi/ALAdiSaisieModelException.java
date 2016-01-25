package ch.globaz.al.business.exceptions.model.adi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier ADI - modèle saisie ADI
 * 
 * @author GMO
 * 
 */
public class ALAdiSaisieModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAdiSaisieModelException() {
        super();

    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAdiSaisieModelException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAdiSaisieModelException(String m, Throwable t) {
        super(m, t);

    }

}
