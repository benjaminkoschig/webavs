/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class FormuleException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FormuleException() {
        super();
    }

    public FormuleException(String m, Throwable t) {
        super(m, t);
    }

    public FormuleException(String m) {
        super(m);
    }
}
