package ch.globaz.al.business.exceptions.model.droit;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des droits
 * 
 * @author jts
 */
public class ALDroitModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDroitModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDroitModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDroitModelException(String m, Throwable t) {
        super(m, t);
    }

}