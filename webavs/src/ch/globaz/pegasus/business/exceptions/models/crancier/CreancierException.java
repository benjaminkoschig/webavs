package ch.globaz.pegasus.business.exceptions.models.crancier;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class CreancierException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CreancierException() {
        super();
    }

    public CreancierException(String m) {
        super(m);
    }

    public CreancierException(String m, Throwable t) {
        super(m, t);
    }
}
