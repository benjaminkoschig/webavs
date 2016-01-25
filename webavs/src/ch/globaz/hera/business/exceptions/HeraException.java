/**
 * 
 */
package ch.globaz.hera.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * @author BSC
 * 
 */
public class HeraException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public HeraException() {
    }

    /**
     * @param m
     */
    public HeraException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public HeraException(String m, Throwable t) {
        super(m, t);
    }

}
