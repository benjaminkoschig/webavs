package ch.globaz.al.business.exceptions.droitEcheance;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion des échéances
 * 
 * @author PTA
 * 
 */
public class ALDroitEcheanceException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDroitEcheanceException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDroitEcheanceException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDroitEcheanceException(String m, Throwable t) {
        super(m, t);

    }

}
