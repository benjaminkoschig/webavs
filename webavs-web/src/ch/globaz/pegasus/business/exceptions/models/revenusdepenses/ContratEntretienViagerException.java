package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class ContratEntretienViagerException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ContratEntretienViagerException() {
        super();
    }

    /**
     * @param m
     */
    public ContratEntretienViagerException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public ContratEntretienViagerException(String m, Throwable t) {
        super(m, t);
    }
}
