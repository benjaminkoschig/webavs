package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des détail des prestations
 * 
 * @author PTA
 * 
 */
public class ALDetailPrestationModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDetailPrestationModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDetailPrestationModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDetailPrestationModelException(String m, Throwable t) {
        super(m, t);
    }
}
