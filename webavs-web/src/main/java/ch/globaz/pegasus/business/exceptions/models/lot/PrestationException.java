package ch.globaz.pegasus.business.exceptions.models.lot;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PrestationException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PrestationException() {
        super();
    }

    public PrestationException(String m) {
        super(m);
    }

    public PrestationException(String m, Throwable t) {
        super(m, t);
    }

}
