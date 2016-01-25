package ch.globaz.corvus.business.exceptions.models;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class RetenuePayementException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RetenuePayementException() {
    }

    public RetenuePayementException(String m) {
        super(m);
    }

    public RetenuePayementException(String m, Throwable t) {
        super(m, t);
    }

}
