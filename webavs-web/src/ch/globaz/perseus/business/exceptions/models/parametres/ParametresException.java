/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.parametres;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class ParametresException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ParametresException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public ParametresException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public ParametresException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
