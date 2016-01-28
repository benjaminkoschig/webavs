package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class BienImmobilierNonHabitableException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public BienImmobilierNonHabitableException() {
        super();
    }

    /**
     * @param m
     */
    public BienImmobilierNonHabitableException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public BienImmobilierNonHabitableException(String m, Throwable t) {
        super(m, t);
    }
}