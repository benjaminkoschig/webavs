package ch.globaz.hera.business.exceptions.models;

import ch.globaz.hera.business.exceptions.HeraException;

public class ConjointException extends HeraException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ConjointException() {
    }

    public ConjointException(String m) {
        super(m);
    }

    public ConjointException(String m, Throwable t) {
        super(m, t);
    }

}
