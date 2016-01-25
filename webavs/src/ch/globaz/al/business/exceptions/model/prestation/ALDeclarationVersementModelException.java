package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des déclaration de versment
 * 
 * @author PTA
 * 
 */
public class ALDeclarationVersementModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDeclarationVersementModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDeclarationVersementModelException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDeclarationVersementModelException(String m, Throwable t) {
        super(m, t);

    }

}
