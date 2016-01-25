package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les commentaires de dossier
 * 
 * @author jts
 */
public class ALCommentaireModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCommentaireModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCommentaireModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCommentaireModelException(String m, Throwable t) {
        super(m, t);
    }
}