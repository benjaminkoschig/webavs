package ch.globaz.pegasus.business.exceptions.models.home;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PrixChambreException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PrixChambreException() {
        super();
    }

    public PrixChambreException(String m) {
        super(m);
    }

    public PrixChambreException(String m, Throwable t) {
        super(m, t);
    }

}
