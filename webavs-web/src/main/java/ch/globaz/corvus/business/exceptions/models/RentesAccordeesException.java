package ch.globaz.corvus.business.exceptions.models;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class RentesAccordeesException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RentesAccordeesException() {
    }

    public RentesAccordeesException(String m) {
        super(m);
    }

    public RentesAccordeesException(String m, Throwable t) {
        super(m, t);
    }

}
