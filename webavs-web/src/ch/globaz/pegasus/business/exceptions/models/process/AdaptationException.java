package ch.globaz.pegasus.business.exceptions.models.process;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AdaptationException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AdaptationException() {
        super();
    }

    public AdaptationException(String m) {
        super(m);
    }

    public AdaptationException(String m, Throwable t) {
        super(m, t);
    }
}
