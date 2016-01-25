/**
 * 
 */
package ch.globaz.al.business.exceptions.model.adi;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier ADI - Prestations par enfant / mois
 * 
 * @author PTA
 * 
 */
public class ALAdiEnfantMoisModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAdiEnfantMoisModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAdiEnfantMoisModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAdiEnfantMoisModelException(String m, Throwable t) {
        super(m, t);
    }

}
