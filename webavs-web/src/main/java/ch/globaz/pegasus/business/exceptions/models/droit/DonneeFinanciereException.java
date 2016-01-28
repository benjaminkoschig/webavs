package ch.globaz.pegasus.business.exceptions.models.droit;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DonneeFinanciereException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DonneeFinanciereException() {
        super();
    }

    /**
     * @param m
     */
    public DonneeFinanciereException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public DonneeFinanciereException(String m, Throwable t) {
        super(m, t);
    }

}
