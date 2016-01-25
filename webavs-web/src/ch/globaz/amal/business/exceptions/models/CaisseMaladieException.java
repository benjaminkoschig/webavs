/**
 * 
 */
package ch.globaz.amal.business.exceptions.models;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author cbu
 * 
 */
public class CaisseMaladieException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CaisseMaladieException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public CaisseMaladieException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public CaisseMaladieException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
