package ch.globaz.corvus.business.exceptions.models;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class SimpleRetenuePayementException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SimpleRetenuePayementException() {
    }

    public SimpleRetenuePayementException(String m) {
        super(m);
    }

    public SimpleRetenuePayementException(String m, Throwable t) {
        super(m, t);
    }

}
