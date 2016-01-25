package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AutresRevenusException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AutresRevenusException() {
        super();
    }

    /**
     * @param m
     */
    public AutresRevenusException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AutresRevenusException(String m, Throwable t) {
        super(m, t);
    }

}
