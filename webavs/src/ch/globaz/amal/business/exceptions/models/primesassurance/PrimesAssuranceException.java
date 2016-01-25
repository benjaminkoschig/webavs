/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.primesassurance;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author cbu
 * 
 */
public class PrimesAssuranceException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PrimesAssuranceException() {
    }

    /**
     * @param m
     */
    public PrimesAssuranceException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public PrimesAssuranceException(String m, Throwable t) {
        super(m, t);
    }

}
