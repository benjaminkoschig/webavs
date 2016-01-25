package ch.globaz.al.business.exceptions.model.tarif;

import ch.globaz.al.business.exceptions.ALException;

/**
 * classe <code>Exception</code> métier pour les services de gestion des échéances
 * 
 * @author PTA
 */
public class ALEcheanceModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEcheanceModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEcheanceModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEcheanceModelException(String m, Throwable t) {
        super(m, t);
    }

}
