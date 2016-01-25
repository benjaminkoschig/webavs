package ch.globaz.al.business.exceptions.model.annonces.rafam;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les annonces RAFAM
 * 
 * @author jts
 */
public class ALAnnonceRafamNotFoundException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAnnonceRafamNotFoundException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAnnonceRafamNotFoundException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAnnonceRafamNotFoundException(String m, Throwable t) {
        super(m, t);
    }

}