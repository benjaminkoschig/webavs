/**
 * 
 */
package ch.globaz.al.business.exceptions.model.envoi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> m�tier Envoi - Erreur li�es aux envois AF WordML
 * 
 * @author dhi
 * 
 */
public class ALEnvoiJobException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnvoiJobException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnvoiJobException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnvoiJobException(String m, Throwable t) {
        super(m, t);
    }

}
