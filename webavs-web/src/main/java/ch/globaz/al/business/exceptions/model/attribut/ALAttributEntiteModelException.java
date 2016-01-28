package ch.globaz.al.business.exceptions.model.attribut;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des attributs
 * 
 * @author GMO
 * 
 */
public class ALAttributEntiteModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAttributEntiteModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAttributEntiteModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAttributEntiteModelException(String m, Throwable t) {
        super(m, t);
    }

}
