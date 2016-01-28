package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class RevenuActiviteLucrativeIndependanteException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RevenuActiviteLucrativeIndependanteException() {
        super();
    }

    /**
     * @param m
     */
    public RevenuActiviteLucrativeIndependanteException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public RevenuActiviteLucrativeIndependanteException(String m, Throwable t) {
        super(m, t);
    }
}
