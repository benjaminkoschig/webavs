package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class RevenuHypothetiqueException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RevenuHypothetiqueException() {
        super();
    }

    /**
     * @param m
     */
    public RevenuHypothetiqueException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public RevenuHypothetiqueException(String m, Throwable t) {
        super(m, t);
    }
}
