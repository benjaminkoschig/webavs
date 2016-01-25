package ch.globaz.amal.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Classe mère de toutes les exceptions métiers
 * 
 * @author CBU
 */
public abstract class AmalException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AmalException() {
        super();
    }

    public AmalException(String m) {
        super(m);
    }

    public AmalException(String m, Throwable t) {
        super(m, t);
    }

}
