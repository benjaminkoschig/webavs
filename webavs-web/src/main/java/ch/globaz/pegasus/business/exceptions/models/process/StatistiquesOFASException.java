package ch.globaz.pegasus.business.exceptions.models.process;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class StatistiquesOFASException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StatistiquesOFASException() {
        super();
    }

    public StatistiquesOFASException(String m) {
        super(m);
    }

    public StatistiquesOFASException(String m, Throwable t) {
        super(m, t);
    }
}
