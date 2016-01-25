package ch.globaz.pegasus.business.exceptions.models.variablemetier;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class VariableMetierException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public VariableMetierException() {
        super();
    }

    public VariableMetierException(String m) {
        super(m);
    }

    public VariableMetierException(String m, Throwable t) {
        super(m, t);
    }
}
