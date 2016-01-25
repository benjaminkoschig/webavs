package ch.globaz.al.business.exceptions.document;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion des adresses des documents
 * 
 * @author PTA
 * 
 */
public class ALDocumentAddressException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDocumentAddressException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDocumentAddressException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDocumentAddressException(String m, Throwable t) {
        super(m, t);

    }

}
