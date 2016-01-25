/**
 * 
 */
package ch.globaz.al.business.exceptions.decision;

import ch.globaz.al.business.exceptions.ALException;

/**
 * 
 * Classe <code>Exception</code> pour la génération des décisions editing
 * 
 * @author pta
 * 
 */
public class ALDecisionEditingException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDecisionEditingException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDecisionEditingException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDecisionEditingException(String m, Throwable t) {
        super(m, t);
    }

}
