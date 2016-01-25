package ch.globaz.pegasus.business.exceptions.models.lot;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class ComptabiliserLotException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ComptabiliserLotException() {
        super();
    }

    public ComptabiliserLotException(String m) {
        super(m);
    }

    public ComptabiliserLotException(String m, Throwable t) {
        super(m, t);
    }
}
