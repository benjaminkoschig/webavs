package ch.globaz.hera.business.exceptions.models;

import ch.globaz.hera.business.exceptions.HeraException;

public class PeriodeException extends HeraException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PeriodeException() {
    }

    public PeriodeException(String m) {
        super(m);
    }

    public PeriodeException(String m, Throwable t) {
        super(m, t);
    }

}
