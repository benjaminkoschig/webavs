/**
 * 
 */
package ch.globaz.perseus.business.exceptions.calcul;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class CalculException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CalculException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public CalculException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public CalculException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
