package ch.globaz.pegasus.business.exceptions.models.habitat;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class LoyerException extends PegasusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LoyerException() {
    }

    public LoyerException(String m) {
        super(m);
    }

    public LoyerException(String m, Throwable t) {
        super(m, t);
    }
}
