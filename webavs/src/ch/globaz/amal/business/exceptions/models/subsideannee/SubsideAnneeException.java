/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.subsideannee;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class SubsideAnneeException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SubsideAnneeException() {
        super();
    }

    public SubsideAnneeException(String m) {
        super(m);
    }

    public SubsideAnneeException(String m, Throwable t) {
        super(m, t);
    }
}
