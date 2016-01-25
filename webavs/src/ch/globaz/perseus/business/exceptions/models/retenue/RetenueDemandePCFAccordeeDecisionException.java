package ch.globaz.perseus.business.exceptions.models.retenue;

import ch.globaz.perseus.business.exceptions.PerseusException;

public class RetenueDemandePCFAccordeeDecisionException extends PerseusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RetenueDemandePCFAccordeeDecisionException() {
    }

    /**
     * @param m
     */
    public RetenueDemandePCFAccordeeDecisionException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public RetenueDemandePCFAccordeeDecisionException(String m, Throwable t) {
        super(m, t);
    }
}
