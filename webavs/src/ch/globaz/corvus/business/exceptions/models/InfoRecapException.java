package ch.globaz.corvus.business.exceptions.models;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class InfoRecapException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InfoRecapException() {
    }

    public InfoRecapException(String m) {
        super(m);
    }

    public InfoRecapException(String m, Throwable t) {
        super(m, t);
    }

}
