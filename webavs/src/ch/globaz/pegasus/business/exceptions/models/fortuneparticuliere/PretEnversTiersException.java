package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PretEnversTiersException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PretEnversTiersException() {
        super();
    }

    /**
     * @param m
     */
    public PretEnversTiersException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public PretEnversTiersException(String m, Throwable t) {
        super(m, t);
    }

}
