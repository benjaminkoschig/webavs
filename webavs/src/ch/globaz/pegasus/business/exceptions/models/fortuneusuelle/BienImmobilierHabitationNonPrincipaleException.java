package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class BienImmobilierHabitationNonPrincipaleException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public BienImmobilierHabitationNonPrincipaleException() {
        super();
    }

    /**
     * @param m
     */
    public BienImmobilierHabitationNonPrincipaleException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public BienImmobilierHabitationNonPrincipaleException(String m, Throwable t) {
        super(m, t);
    }
}
