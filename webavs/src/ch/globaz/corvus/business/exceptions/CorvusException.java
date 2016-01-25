/**
 * 
 */
package ch.globaz.corvus.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * @author BSC
 * 
 */
public class CorvusException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CorvusException() {
    }

    /**
     * @param m
     */
    public CorvusException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public CorvusException(String m, Throwable t) {
        super(m, t);
    }

}
