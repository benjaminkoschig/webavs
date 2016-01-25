/**
 * 
 */
package globaz.apg.exceptions;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 */
public class APAnnoncesException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param m
     */
    public APAnnoncesException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public APAnnoncesException(String m, Throwable t) {
        super(m, t);
    }

}
