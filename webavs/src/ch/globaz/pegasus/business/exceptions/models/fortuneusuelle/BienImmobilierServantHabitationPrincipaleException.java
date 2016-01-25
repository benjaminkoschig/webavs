package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class BienImmobilierServantHabitationPrincipaleException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public BienImmobilierServantHabitationPrincipaleException() {
        super();
    }

    /**
     * @param m
     */
    public BienImmobilierServantHabitationPrincipaleException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public BienImmobilierServantHabitationPrincipaleException(String m, Throwable t) {
        super(m, t);
    }
}