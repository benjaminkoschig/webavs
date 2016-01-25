/**
 * 
 */
package ch.globaz.al.business.exceptions.model.tarif;

import ch.globaz.al.business.exceptions.ALException;

/**
 * classe des exceptions métier de Prestation Tarif
 * 
 * @author PTA
 * 
 */
public class ALPrestationTarifModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALPrestationTarifModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALPrestationTarifModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALPrestationTarifModelException(String m, Throwable t) {
        super(m, t);
    }

}
