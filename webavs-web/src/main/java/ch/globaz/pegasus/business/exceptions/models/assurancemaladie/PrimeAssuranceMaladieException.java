package ch.globaz.pegasus.business.exceptions.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PrimeAssuranceMaladieException extends PegasusException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
	 *
	 */
    public PrimeAssuranceMaladieException() {
        super();
    }

    /**
     * @param m
     */
    public PrimeAssuranceMaladieException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public PrimeAssuranceMaladieException(String m, Throwable t) {
        super(m, t);
    }

}
