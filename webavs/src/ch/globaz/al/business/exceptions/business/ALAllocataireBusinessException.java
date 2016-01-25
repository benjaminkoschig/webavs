package ch.globaz.al.business.exceptions.business;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés aux allocataires
 * 
 * @author jts
 */
public class ALAllocataireBusinessException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAllocataireBusinessException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAllocataireBusinessException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAllocataireBusinessException(String m, Throwable t) {
        super(m, t);
    }
}
