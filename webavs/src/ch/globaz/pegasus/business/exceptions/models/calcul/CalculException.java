package ch.globaz.pegasus.business.exceptions.models.calcul;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class CalculException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] parameters;

    public CalculException() {
    }

    public CalculException(String m) {
        super(m);
    }

    public CalculException(String m, String... args) {
        this(m);
        parameters = args;
    }

    public CalculException(String m, Throwable t) {
        super(m, t);
    }

    public CalculException(String m, Throwable t, String... args) {
        this(m, t);
        parameters = args;
    }

    public String[] getParameters() {
        return parameters;
    }

}
