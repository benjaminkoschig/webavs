package ch.globaz.pegasus.business.exceptions.models.decompte;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DecompteException extends PegasusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] parameters = null;

    public DecompteException() {
    }

    public DecompteException(String m) {
        super(m);
    }

    public DecompteException(String m, Throwable t) {
        super(m, t);
    }

    public DecompteException(String[] parameters) {
        this.parameters = parameters;
    }

    public String[] getParameters() {
        return parameters;
    }
}
