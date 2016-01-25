package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class RevenuActiviteLucrativeDependanteException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RevenuActiviteLucrativeDependanteException() {
        super();
    }

    /**
     * @param m
     */
    public RevenuActiviteLucrativeDependanteException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public RevenuActiviteLucrativeDependanteException(String m, Throwable t) {
        super(m, t);
    }
}
