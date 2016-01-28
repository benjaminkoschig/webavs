/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.retenue;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class RetenueException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RetenueException() {
    }

    /**
     * @param m
     */
    public RetenueException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public RetenueException(String m, Throwable t) {
        super(m, t);
    }

}
