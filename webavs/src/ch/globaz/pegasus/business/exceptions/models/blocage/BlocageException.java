package ch.globaz.pegasus.business.exceptions.models.blocage;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class BlocageException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BlocageException() {
        super();
    }

    public BlocageException(String m) {
        super(m);
    }

    public BlocageException(String m, Throwable t) {
        super(m, t);
    }
}
