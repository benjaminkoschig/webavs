package ch.globaz.pegasus.business.exceptions.models.droit;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class VersionDroitException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public VersionDroitException() {
        super();
    }

    /**
     * @param m
     */
    public VersionDroitException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public VersionDroitException(String m, Throwable t) {
        super(m, t);
    }

}
