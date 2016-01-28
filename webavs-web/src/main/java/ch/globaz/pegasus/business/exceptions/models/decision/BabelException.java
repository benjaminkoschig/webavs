/**
 * 
 */
package ch.globaz.pegasus.business.exceptions.models.decision;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * @author SCE
 * 
 *         15 juil. 2010
 */
public class BabelException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] parameters = null;

    public BabelException() {
    }

    public BabelException(String m) {
        super(m);
    }

    public BabelException(String m, Throwable t) {
        super(m, t);
    }

    public BabelException(String[] parameters) {
        this.parameters = parameters;
    }

    public String[] getParameters() {
        return parameters;
    }
}
