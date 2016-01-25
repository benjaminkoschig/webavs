/**
 * 
 */
package ch.globaz.al.business.exceptions.model.tarif;

import ch.globaz.al.business.exceptions.ALException;

/**
 * classe exception PrestationTarifComplexModel
 * 
 * @author PTA
 * 
 */
public class ALTarifComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALTarifComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALTarifComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALTarifComplexModelException(String m, Throwable t) {
        super(m, t);
    }

}
