/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author LFO
 * 
 */
public class DefinitionFormuleException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DefinitionFormuleException() {
        super();
    }

    public DefinitionFormuleException(String m) {
        super(m);
    }

    public DefinitionFormuleException(String m, Throwable t) {
        super(m, t);
    }
}
