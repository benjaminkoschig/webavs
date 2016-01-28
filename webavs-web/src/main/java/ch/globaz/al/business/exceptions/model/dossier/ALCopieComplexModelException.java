package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> m�tier pour les copies des documents li�es aux dossiers
 * 
 * @author JER
 */
public class ALCopieComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCopieComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCopieComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCopieComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}
