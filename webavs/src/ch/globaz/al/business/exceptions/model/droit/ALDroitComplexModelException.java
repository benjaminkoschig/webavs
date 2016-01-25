package ch.globaz.al.business.exceptions.model.droit;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> ListDroitsComplexModel
 * 
 * @author PTA
 * 
 */
public class ALDroitComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDroitComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDroitComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDroitComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}
