/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.parametreModel;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class ParametreModelException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ParametreModelException() {
        super();
    }

    public ParametreModelException(String m) {
        super(m);
    }

    public ParametreModelException(String m, Throwable t) {
        super(m, t);
    }
}
