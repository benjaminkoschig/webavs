package ch.globaz.al.business.exceptions.model.annonces.rafam;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les annonces RAFAM
 * 
 * @author jts
 */
public class ALAnnonceRafamException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAnnonceRafamException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAnnonceRafamException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAnnonceRafamException(String m, Throwable t) {
        super(m, t);
    }

}