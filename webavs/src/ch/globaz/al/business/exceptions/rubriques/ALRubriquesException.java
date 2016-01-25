package ch.globaz.al.business.exceptions.rubriques;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion des rubriques
 * 
 * @author jts
 * 
 */
public class ALRubriquesException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRubriquesException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRubriquesException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRubriquesException(String m, Throwable t) {
        super(m, t);

    }

}
