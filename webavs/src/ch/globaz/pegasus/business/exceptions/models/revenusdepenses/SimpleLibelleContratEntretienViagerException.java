package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class SimpleLibelleContratEntretienViagerException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public SimpleLibelleContratEntretienViagerException() {
        super();
    }

    /**
     * @param m
     */
    public SimpleLibelleContratEntretienViagerException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public SimpleLibelleContratEntretienViagerException(String m, Throwable t) {
        super(m, t);
    }
}
