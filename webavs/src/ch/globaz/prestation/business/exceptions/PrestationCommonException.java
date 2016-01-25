/**
 * 
 */
package ch.globaz.prestation.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * @author ECO
 * 
 */
public class PrestationCommonException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PrestationCommonException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public PrestationCommonException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public PrestationCommonException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
