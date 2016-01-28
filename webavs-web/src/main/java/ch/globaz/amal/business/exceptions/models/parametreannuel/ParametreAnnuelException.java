/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.parametreannuel;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class ParametreAnnuelException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ParametreAnnuelException() {
        super();
    }

    public ParametreAnnuelException(String m) {
        super(m);
    }

    public ParametreAnnuelException(String m, Throwable t) {
        super(m, t);
    }

}
