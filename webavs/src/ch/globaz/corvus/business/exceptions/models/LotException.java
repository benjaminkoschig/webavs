package ch.globaz.corvus.business.exceptions.models;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class LotException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LotException() {
    }

    public LotException(String m) {
        super(m);
    }

    public LotException(String m, Throwable t) {
        super(m, t);
    }

}
