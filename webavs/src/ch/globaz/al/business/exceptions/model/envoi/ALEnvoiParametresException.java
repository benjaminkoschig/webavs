/**
 * 
 */
package ch.globaz.al.business.exceptions.model.envoi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * 
 * Classe <code>Exception</code> métier paramètres envoi
 * 
 * @author dhi
 * 
 */
public class ALEnvoiParametresException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnvoiParametresException() {
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnvoiParametresException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnvoiParametresException(String m, Throwable t) {
        super(m, t);
    }

}
