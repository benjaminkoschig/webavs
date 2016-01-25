package ch.globaz.perseus.business.exceptions.models.impotsource;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * 
 * @author MBO
 * 
 */

public class TauxException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TauxException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public TauxException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public TauxException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
