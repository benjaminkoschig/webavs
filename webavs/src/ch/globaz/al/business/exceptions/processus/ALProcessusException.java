package ch.globaz.al.business.exceptions.processus;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés à la gestion des processus
 * 
 * @author gmo
 */
public class ALProcessusException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALProcessusException() {
        super();

    }

    /**
     * @see ALException#ALException(String)
     */
    public ALProcessusException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALProcessusException(String m, Throwable t) {
        super(m, t);
    }
}
