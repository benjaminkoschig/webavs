/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.primeavantageuse;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author CBU
 * 
 */
public class PrimeAvantageuseException extends AmalException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PrimeAvantageuseException() {
        super();
    }

    public PrimeAvantageuseException(String m) {
        super(m);
    }

    public PrimeAvantageuseException(String m, Throwable t) {
        super(m, t);
    }
}
