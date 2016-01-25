/**
 * 
 */
package ch.globaz.al.business.exceptions.model.tarif;

import ch.globaz.al.business.exceptions.ALException;

/**
 * @author PTA
 * 
 */
public class ALCritereTarifModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCritereTarifModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCritereTarifModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCritereTarifModelException(String m, Throwable t) {
        super(m, t);
    }

}
