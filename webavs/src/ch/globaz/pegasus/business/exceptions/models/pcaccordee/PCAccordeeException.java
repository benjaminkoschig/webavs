package ch.globaz.pegasus.business.exceptions.models.pcaccordee;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PCAccordeeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PCAccordeeException() {
        super();
    }

    public PCAccordeeException(String m) {
        super(m);
    }

    public PCAccordeeException(String m, Throwable t) {
        super(m, t);
    }

}
