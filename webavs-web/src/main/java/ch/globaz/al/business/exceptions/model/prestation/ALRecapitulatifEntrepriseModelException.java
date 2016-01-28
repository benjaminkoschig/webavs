package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * classe <code>Exception</code> métier sur RecapitulatifEntreprise
 * 
 * @author PTA
 */
public class ALRecapitulatifEntrepriseModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRecapitulatifEntrepriseModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRecapitulatifEntrepriseModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRecapitulatifEntrepriseModelException(String m, Throwable t) {
        super(m, t);
    }
}
