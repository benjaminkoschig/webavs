/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.annonce;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author dhi
 * 
 */
public class AnnonceException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AnnonceException() {
    }

    /**
     * @param m
     */
    public AnnonceException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AnnonceException(String m, Throwable t) {
        super(m, t);
    }

}
