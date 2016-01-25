package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PensionAlimentaireException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PensionAlimentaireException() {
        super();
    }

    /**
     * @param m
     */
    public PensionAlimentaireException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public PensionAlimentaireException(String m, Throwable t) {
        super(m, t);
    }
}
