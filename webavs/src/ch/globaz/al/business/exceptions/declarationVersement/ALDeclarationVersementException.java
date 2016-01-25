package ch.globaz.al.business.exceptions.declarationVersement;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion des déclarations de versement
 * 
 * @author PTA
 * 
 */
public class ALDeclarationVersementException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDeclarationVersementException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDeclarationVersementException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDeclarationVersementException(String m, Throwable t) {
        super(m, t);

    }

}
