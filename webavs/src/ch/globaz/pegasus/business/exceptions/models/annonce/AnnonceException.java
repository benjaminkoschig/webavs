package ch.globaz.pegasus.business.exceptions.models.annonce;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AnnonceException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AnnonceException() {
        super();
    }

    public AnnonceException(String m) {
        super(m);
    }

    public AnnonceException(String m, Throwable t) {
        super(m, t);
    }

}
