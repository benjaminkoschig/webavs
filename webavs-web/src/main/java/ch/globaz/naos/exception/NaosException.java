package ch.globaz.naos.exception;

import globaz.jade.exception.JadeApplicationException;

public class NaosException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NaosException(String message) {
        super(message);
    }

    public NaosException(String msg, Exception e) {
        super(msg, e);
    }

}
