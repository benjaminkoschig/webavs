package ch.globaz.al.business.exceptions.model.droit;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> EnfantComplexModel
 * 
 * @author GMO
 * 
 */
public class ALEnfantComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnfantComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnfantComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnfantComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}
