package ch.globaz.babel.business.exception;

import globaz.jade.exception.JadeApplicationException;

public class CatalogueTexteException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CatalogueTexteException(String message) {
        super(message);
    }

    public CatalogueTexteException(String msg, Exception e) {
        super(msg, e);
    }
}
