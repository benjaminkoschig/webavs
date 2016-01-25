package ch.globaz.al.business.exceptions.enteteDocument;

import ch.globaz.al.business.exceptions.ALException;

/**
 * * Classe <code>Exception</code> pour la gestion des entetes des documents
 * 
 * @author PTA
 * 
 */

public class ALEnteteDocumentException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnteteDocumentException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnteteDocumentException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnteteDocumentException(String m, Throwable t) {
        super(m, t);

    }

}
