package ch.globaz.pegasus.business.exceptions.models.avance;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AvanceException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AvanceException() {
        super();
    }

    public AvanceException(String m) {
        super(m);
    }

    public AvanceException(String m, Throwable t) {
        super(m, t);
    }
}
