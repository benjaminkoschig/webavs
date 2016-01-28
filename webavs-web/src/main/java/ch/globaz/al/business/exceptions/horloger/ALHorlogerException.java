package ch.globaz.al.business.exceptions.horloger;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les services sp�cifiques aux caisses horlog�res
 * 
 * @author jts
 * 
 */
public class ALHorlogerException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALHorlogerException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALHorlogerException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALHorlogerException(String m, Throwable t) {
        super(m, t);

    }
}