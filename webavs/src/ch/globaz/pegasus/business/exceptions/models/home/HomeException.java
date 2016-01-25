package ch.globaz.pegasus.business.exceptions.models.home;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class HomeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public HomeException() {
        super();
    }

    public HomeException(String m) {
        super(m);
    }

    public HomeException(String m, Throwable t) {
        super(m, t);
    }

}
