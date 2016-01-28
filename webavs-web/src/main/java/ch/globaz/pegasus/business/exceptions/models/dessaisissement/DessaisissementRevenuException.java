package ch.globaz.pegasus.business.exceptions.models.dessaisissement;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DessaisissementRevenuException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DessaisissementRevenuException() {
        super();
    }

    /**
     * @param m
     */
    public DessaisissementRevenuException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public DessaisissementRevenuException(String m, Throwable t) {
        super(m, t);
    }

}
