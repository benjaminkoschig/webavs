/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.primemoyenne;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class PrimeMoyenneException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PrimeMoyenneException() {
        super();
    }

    public PrimeMoyenneException(String m) {
        super(m);
    }

    public PrimeMoyenneException(String m, Throwable t) {
        super(m, t);
    }
}
