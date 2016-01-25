package ch.globaz.perseus.business.exceptions.models.lot;

import ch.globaz.perseus.business.exceptions.PerseusException;

public class OrdreVersementException extends PerseusException {

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
