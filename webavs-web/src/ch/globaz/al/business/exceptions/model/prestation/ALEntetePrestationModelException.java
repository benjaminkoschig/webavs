package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Class <code>Exception</code> métier des En-tête de prestation
 * 
 * @author PTA
 * 
 */
public class ALEntetePrestationModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEntetePrestationModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEntetePrestationModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEntetePrestationModelException(String m, Throwable t) {
        super(m, t);
    }

}
