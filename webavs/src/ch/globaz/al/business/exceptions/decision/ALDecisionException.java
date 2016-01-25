package ch.globaz.al.business.exceptions.decision;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la g�n�ration des d�cisions
 * 
 * @author jer
 */
public class ALDecisionException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDecisionException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDecisionException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDecisionException(String m, Throwable t) {
        super(m, t);
    }
}
