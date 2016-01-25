/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.annoncesedex;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author dhi
 * 
 */
public class AnnonceSedexException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public AnnonceSedexException() {
    }

    /**
     * @param m
     */
    public AnnonceSedexException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AnnonceSedexException(String m, Throwable t) {
        super(m, t);
    }

}
