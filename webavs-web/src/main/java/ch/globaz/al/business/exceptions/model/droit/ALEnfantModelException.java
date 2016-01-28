package ch.globaz.al.business.exceptions.model.droit;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des enfants
 * 
 * @author jts
 */
public class ALEnfantModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnfantModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnfantModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnfantModelException(String m, Throwable t) {
        super(m, t);
    }
}