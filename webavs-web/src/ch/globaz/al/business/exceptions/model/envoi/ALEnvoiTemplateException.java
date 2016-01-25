/**
 * 
 */
package ch.globaz.al.business.exceptions.model.envoi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> m�tier Envoi - Erreur li�es aux templates AF WordML
 * 
 * @author dhi
 * 
 */
public class ALEnvoiTemplateException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnvoiTemplateException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnvoiTemplateException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnvoiTemplateException(String m, Throwable t) {
        super(m, t);
    }

}
