package ch.globaz.al.business.exceptions.model.periodeAF;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> PeriodeAFModel
 * 
 * @author GMO
 * 
 */
public class ALPeriodeAFException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALPeriodeAFException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALPeriodeAFException(String m) {
        super(m);
    }

    /**
     * 
     * @see ALException#ALException(String, Throwable)
     */
    public ALPeriodeAFException(String m, Throwable t) {
        super(m, t);
    }
}
