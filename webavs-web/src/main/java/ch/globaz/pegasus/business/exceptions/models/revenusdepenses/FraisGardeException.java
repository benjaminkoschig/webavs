package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class FraisGardeException extends PegasusException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public FraisGardeException() {
        super();
    }

    /**
     * @param m
     */
    public FraisGardeException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public FraisGardeException(String m, Throwable t) {
        super(m, t);
    }
}
