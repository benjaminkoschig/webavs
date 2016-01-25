package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AllocationsFamilialesException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AllocationsFamilialesException() {
        super();
    }

    /**
     * @param m
     */
    public AllocationsFamilialesException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AllocationsFamilialesException(String m, Throwable t) {
        super(m, t);
    }
}
