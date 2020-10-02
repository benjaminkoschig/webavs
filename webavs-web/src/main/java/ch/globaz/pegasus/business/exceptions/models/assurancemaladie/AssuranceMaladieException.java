package ch.globaz.pegasus.business.exceptions.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AssuranceMaladieException extends PegasusException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
	 *
	 */
    public AssuranceMaladieException() {
        super();
    }

    /**
     * @param m
     */
    public AssuranceMaladieException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AssuranceMaladieException(String m, Throwable t) {
        super(m, t);
    }

}
