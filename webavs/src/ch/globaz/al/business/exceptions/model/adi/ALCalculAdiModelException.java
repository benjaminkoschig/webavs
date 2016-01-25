/**
 * 
 */
package ch.globaz.al.business.exceptions.model.adi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> m�tier ADI - mod�le Calcul
 * 
 * @author PTA
 * 
 */
public class ALCalculAdiModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCalculAdiModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCalculAdiModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCalculAdiModelException(String m, Throwable t) {
        super(m, t);
    }

}
