package ch.globaz.pegasus.business.exceptions.models.droit;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DroitException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DroitException() {
        super();
    }

    /**
     * @param m
     */
    public DroitException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public DroitException(String m, Throwable t) {
        super(m, t);
    }

}
