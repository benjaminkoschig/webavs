package ch.globaz.pegasus.business.exceptions.models.dettecomptatcompense;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DetteComptatCompenseException extends PegasusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DetteComptatCompenseException() {
        super();
    }

    public DetteComptatCompenseException(String m) {
        super(m);
    }

    public DetteComptatCompenseException(String m, Throwable t) {
        super(m, t);
    }
}
