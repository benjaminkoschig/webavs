package ch.globaz.pegasus.business.exceptions.models;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class MutationException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public MutationException() {
        super();
    }

    /**
     * @param m
     */
    public MutationException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public MutationException(String m, Throwable t) {
        super(m, t);
    }
}
