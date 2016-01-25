package ch.globaz.pegasus.business.exceptions.models.revisionquadriennale;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class RevisionQuadriennaleException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] parameters;

    public RevisionQuadriennaleException() {
    }

    public RevisionQuadriennaleException(String m) {
        super(m);
    }

    public RevisionQuadriennaleException(String m, String... args) {
        this(m);
        parameters = args;
    }

    public RevisionQuadriennaleException(String m, Throwable t) {
        super(m, t);
    }

    public RevisionQuadriennaleException(String m, Throwable t, String... args) {
        this(m, t);
        parameters = args;
    }

    public String[] getParameters() {
        return parameters;
    }

}
