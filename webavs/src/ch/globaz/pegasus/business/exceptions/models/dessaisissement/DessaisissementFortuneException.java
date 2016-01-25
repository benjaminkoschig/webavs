package ch.globaz.pegasus.business.exceptions.models.dessaisissement;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DessaisissementFortuneException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DessaisissementFortuneException() {
        super();
    }

    /**
     * @param m
     */
    public DessaisissementFortuneException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public DessaisissementFortuneException(String m, Throwable t) {
        super(m, t);
    }

}
