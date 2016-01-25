/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.creancier;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author MBO
 * 
 */
public class CreancierException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CreancierException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public CreancierException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public CreancierException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
