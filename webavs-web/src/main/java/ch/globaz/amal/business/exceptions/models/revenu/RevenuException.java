package ch.globaz.amal.business.exceptions.models.revenu;

import ch.globaz.amal.business.exceptions.AmalException;

public class RevenuException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RevenuException() {
        super();
    }

    public RevenuException(String m) {
        super(m);
    }

    public RevenuException(String m, Throwable t) {
        super(m, t);
    }
}
