package ch.globaz.al.business.exceptions.tauxMonnaieEtrangere;

import ch.globaz.al.business.exceptions.ALException;

/**
 * * Classe <code>Exception</code> pour le Taux des monnaies étrangères
 * 
 * @author PTA
 * 
 */

public class ALTauxMonnaieEtrangereException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALTauxMonnaieEtrangereException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALTauxMonnaieEtrangereException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALTauxMonnaieEtrangereException(String m, Throwable t) {
        super(m, t);
    }

}
