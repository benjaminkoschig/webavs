package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class CotisationsPsalException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CotisationsPsalException() {
        super();
    }

    /**
     * @param m
     */
    public CotisationsPsalException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public CotisationsPsalException(String m, Throwable t) {
        super(m, t);
    }
}
