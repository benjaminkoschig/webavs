package ch.globaz.al.business.exceptions.document;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion des documents
 * 
 * @author PTA
 * 
 */
public class ALDocumentException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDocumentException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDocumentException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDocumentException(String m, Throwable t) {
        super(m, t);

    }

}
