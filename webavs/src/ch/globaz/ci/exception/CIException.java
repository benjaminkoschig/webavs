package ch.globaz.ci.exception;

import globaz.jade.exception.JadeApplicationException;

public class CIException extends JadeApplicationException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIException(String message) {
        super(message);
    }

    public CIException(String msg, Exception e) {
        super(msg, e);
    }

}
