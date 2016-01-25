package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les copies des documents liées aux dossiers
 * 
 * @author jts
 */
public class ALCopieModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCopieModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCopieModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCopieModelException(String m, Throwable t) {
        super(m, t);
    }

}