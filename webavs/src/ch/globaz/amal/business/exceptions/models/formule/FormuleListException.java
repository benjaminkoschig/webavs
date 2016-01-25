/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author LFO
 * 
 */
public class FormuleListException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FormuleListException() {
        super();
    }

    public FormuleListException(String m) {
        super(m);
    }

    public FormuleListException(String m, Throwable t) {
        super(m, t);
    }
}
