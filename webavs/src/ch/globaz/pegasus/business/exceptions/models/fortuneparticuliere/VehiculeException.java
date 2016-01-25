package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class VehiculeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public VehiculeException() {
        super();
    }

    /**
     * @param m
     */
    public VehiculeException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public VehiculeException(String m, Throwable t) {
        super(m, t);
    }

}
