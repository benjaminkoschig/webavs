package ch.globaz.amal.business.exceptions.models.contribuable;

import ch.globaz.amal.business.exceptions.AmalException;

public class ContribuableException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ContribuableException() {
        super();
    }

    public ContribuableException(String m) {
        super(m);
    }

    public ContribuableException(String m, Throwable t) {
        super(m, t);
    }
}
