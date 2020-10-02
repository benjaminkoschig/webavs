package ch.globaz.pegasus.business.exceptions.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class SubsideAssuranceMaladieException extends PegasusException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
	 *
	 */
    public SubsideAssuranceMaladieException() {
        super();
    }

    /**
     * @param m
     */
    public SubsideAssuranceMaladieException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public SubsideAssuranceMaladieException(String m, Throwable t) {
        super(m, t);
    }

}
