package ch.globaz.al.business.exceptions.rafam;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour l'envoi et la réception d'annonce RAFam
 * 
 * @author jts
 */
public class ALRafamSedexException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRafamSedexException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRafamSedexException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRafamSedexException(String m, Throwable t) {
        super(m, t);
    }

}