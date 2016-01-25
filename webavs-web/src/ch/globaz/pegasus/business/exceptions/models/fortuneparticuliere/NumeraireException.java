package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class NumeraireException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public NumeraireException() {
        super();
    }

    /**
     * @param m
     */
    public NumeraireException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public NumeraireException(String m, Throwable t) {
        super(m, t);
    }

}
