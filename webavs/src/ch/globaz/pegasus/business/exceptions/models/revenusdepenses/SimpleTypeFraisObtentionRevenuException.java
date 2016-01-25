package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class SimpleTypeFraisObtentionRevenuException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public SimpleTypeFraisObtentionRevenuException() {
        super();
    }

    /**
     * @param m
     */
    public SimpleTypeFraisObtentionRevenuException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public SimpleTypeFraisObtentionRevenuException(String m, Throwable t) {
        super(m, t);
    }
}
