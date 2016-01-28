package ch.globaz.al.business.exceptions.copies;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les copies des documents liées aux dossiers
 * 
 * @author jts
 */
public class ALCopieBusinessException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCopieBusinessException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCopieBusinessException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCopieBusinessException(String m, Throwable t) {
        super(m, t);
    }

}