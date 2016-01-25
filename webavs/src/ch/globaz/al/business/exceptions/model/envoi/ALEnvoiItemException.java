/**
 * 
 */
package ch.globaz.al.business.exceptions.model.envoi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier Envoi - Erreur liées aux envois AF WordML
 * 
 * @author dhi
 * 
 */
public class ALEnvoiItemException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnvoiItemException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnvoiItemException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnvoiItemException(String m, Throwable t) {
        super(m, t);
    }

}
