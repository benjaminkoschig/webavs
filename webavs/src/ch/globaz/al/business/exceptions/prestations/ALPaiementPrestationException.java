package ch.globaz.al.business.exceptions.prestations;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour le paiement de prestations
 * 
 * @author jts
 */
public class ALPaiementPrestationException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALPaiementPrestationException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALPaiementPrestationException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALPaiementPrestationException(String m, Throwable t) {
        super(m, t);
    }
}
