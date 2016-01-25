/**
 * 
 */
package ch.globaz.al.business.exceptions.model.tarif;

import ch.globaz.al.business.exceptions.ALException;

/**
 * classe exceptions métier de Legislation tarif
 * 
 * @author PTA
 * 
 */
public class ALLegislationTarifModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALLegislationTarifModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALLegislationTarifModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALLegislationTarifModelException(String m, Throwable t) {
        super(m, t);
    }

}
