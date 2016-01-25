package ch.globaz.lyra.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

public class LYException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LYException() {
        super();
    }

    public LYException(String message) {
        super(message);
    }

    public LYException(String message, Throwable cause) {
        super(message, cause);
    }
}
