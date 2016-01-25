package ch.globaz.orion.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

public abstract class OrionException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OrionException() {
        super();
    }

    public OrionException(String m) {
        super(m);
    }

    public OrionException(String m, Throwable t) {
        super(m, t);
    }

}
