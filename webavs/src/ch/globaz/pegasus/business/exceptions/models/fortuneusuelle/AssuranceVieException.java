package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AssuranceVieException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AssuranceVieException() {
        super();
    }

    /**
     * @param m
     */
    public AssuranceVieException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AssuranceVieException(String m, Throwable t) {
        super(m, t);
    }
}