package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class CapitalLPPException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CapitalLPPException() {
        super();
    }

    /**
     * @param m
     */
    public CapitalLPPException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public CapitalLPPException(String m, Throwable t) {
        super(m, t);
    }
}