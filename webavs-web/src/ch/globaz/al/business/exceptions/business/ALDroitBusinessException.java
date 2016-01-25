package ch.globaz.al.business.exceptions.business;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés aux droits
 * 
 * @author jts
 */
public class ALDroitBusinessException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDroitBusinessException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDroitBusinessException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDroitBusinessException(String m, Throwable t) {
        super(m, t);
    }
}
