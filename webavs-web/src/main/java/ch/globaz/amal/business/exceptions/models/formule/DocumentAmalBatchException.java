/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author LFO
 * 
 */
public class DocumentAmalBatchException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DocumentAmalBatchException() {
        super();
    }

    public DocumentAmalBatchException(String m) {
        super(m);
    }

    public DocumentAmalBatchException(String m, Throwable t) {
        super(m, t);
    }
}
