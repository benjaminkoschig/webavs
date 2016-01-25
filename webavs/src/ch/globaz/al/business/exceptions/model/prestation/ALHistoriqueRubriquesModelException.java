package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier de l'historique des rubriques
 * 
 * @author PTA
 * 
 */
public class ALHistoriqueRubriquesModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALHistoriqueRubriquesModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALHistoriqueRubriquesModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALHistoriqueRubriquesModelException(String m, Throwable t) {
        super(m, t);
    }
}
