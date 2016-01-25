package ch.globaz.pegasus.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Classe mère de toutes les exceptions métiers PC
 * 
 * @author ECO
 */
public abstract class PegasusException extends JadeApplicationException {

    private static final long serialVersionUID = 5953544850820530217L;

    private String[] parameters = null;

    public PegasusException() {
        super();
    }

    public PegasusException(String m) {
        super(m);
    }

    public PegasusException(String m, Throwable t) {
        super(m, t);
    }

    public PegasusException(String[] parameters) {
        this.parameters = parameters;
    }

    public String[] getParameters() {
        return parameters;
    }

}
