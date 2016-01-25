/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.parametreapplication;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author dhi
 * 
 */
public class ParametreApplicationException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public ParametreApplicationException() {
        super();
    }

    /**
     * Constructor with message
     * 
     * @param m
     */
    public ParametreApplicationException(String m) {
        super(m);
    }

    /**
     * Constructor with message and exception
     * 
     * @param m
     * @param t
     */
    public ParametreApplicationException(String m, Throwable t) {
        super(m, t);
    }

}
