package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class TitreException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TitreException() {
        super();
    }

    /**
     * @param m
     */
    public TitreException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public TitreException(String m, Throwable t) {
        super(m, t);
    }
}