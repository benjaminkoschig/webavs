package ch.globaz.perseus.business.exceptions.models.variablemetier;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class VariableMetierException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public VariableMetierException() {
        super();
    }

    /**
     * @param m
     */
    public VariableMetierException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public VariableMetierException(String m, Throwable t) {
        super(m, t);
    }

}
