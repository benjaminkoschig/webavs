package ch.globaz.pegasus.business.exceptions.models.revenusdepenses;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class TypeFraisObtentionRevenuException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TypeFraisObtentionRevenuException() {
        super();
    }

    /**
     * @param m
     */
    public TypeFraisObtentionRevenuException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public TypeFraisObtentionRevenuException(String m, Throwable t) {
        super(m, t);
    }
}
