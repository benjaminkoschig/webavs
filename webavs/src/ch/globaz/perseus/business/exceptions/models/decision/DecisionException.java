package ch.globaz.perseus.business.exceptions.models.decision;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * 
 * @author MBO
 * 
 */

public class DecisionException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DecisionException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public DecisionException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public DecisionException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
