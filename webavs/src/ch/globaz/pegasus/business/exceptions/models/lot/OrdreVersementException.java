package ch.globaz.pegasus.business.exceptions.models.lot;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class OrdreVersementException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OrdreVersementException() {
        super();
    }

    public OrdreVersementException(String m) {
        super(m);
    }

    public OrdreVersementException(String m, Throwable t) {
        super(m, t);
    }

}
