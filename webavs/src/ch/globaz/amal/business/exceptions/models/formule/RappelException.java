/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class RappelException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RappelException() {
        super();
    }

    public RappelException(String m, Throwable t) {
        super(m, t);
    }

    public RappelException(String m) {
        super(m);
    }
}
