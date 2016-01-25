package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class CompteBancaireCCPException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CompteBancaireCCPException() {
        super();
    }

    /**
     * @param m
     */
    public CompteBancaireCCPException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public CompteBancaireCCPException(String m, Throwable t) {
        super(m, t);
    }
}