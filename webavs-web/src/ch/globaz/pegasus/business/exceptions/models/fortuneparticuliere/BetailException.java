package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class BetailException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public BetailException() {
        super();
    }

    /**
     * @param m
     */
    public BetailException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public BetailException(String m, Throwable t) {
        super(m, t);
    }

}
