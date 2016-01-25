package ch.globaz.pegasus.business.exceptions.models.annonce;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class SequenceException extends PegasusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SequenceException() {
        super();
    }

    public SequenceException(String m) {
        super(m);
    }

    public SequenceException(String m, Throwable t) {
        super(m, t);
    }

}
