/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author LFO
 * 
 */
public class DocumentMergeException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DocumentMergeException() {
        super();
    }

    public DocumentMergeException(String m) {
        super(m);
    }

    public DocumentMergeException(String m, Throwable t) {
        super(m, t);
    }
}
