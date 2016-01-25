/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.formule;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author LFO
 * 
 */
public class HistoriqueImportationException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public HistoriqueImportationException() {
        super();
    }

    public HistoriqueImportationException(String m) {
        super(m);
    }

    public HistoriqueImportationException(String m, Throwable t) {
        super(m, t);
    }
}
