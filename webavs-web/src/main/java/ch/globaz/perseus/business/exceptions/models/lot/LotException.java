package ch.globaz.perseus.business.exceptions.models.lot;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * 
 * @author MBO
 * 
 */

public class LotException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public LotException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public LotException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public LotException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
