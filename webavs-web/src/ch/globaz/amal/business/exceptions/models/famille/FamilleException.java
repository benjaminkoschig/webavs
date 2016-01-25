package ch.globaz.amal.business.exceptions.models.famille;

import ch.globaz.amal.business.exceptions.AmalException;

public class FamilleException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FamilleException() {
        super();
    }

    public FamilleException(String m) {
        super(m);
    }

    public FamilleException(String m, Throwable t) {
        super(m, t);
    }
}
