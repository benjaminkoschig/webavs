/**
 * 
 */
package ch.globaz.pegasus.business.exceptions.models.decision;

import globaz.jade.context.JadeThread;
import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * @author SCE
 * 
 *         15 juil. 2010
 */
public class DecisionException extends PegasusException {

    private static final long serialVersionUID = -9000939656276239561L;

    public DecisionException() {
    }

    public DecisionException(String m) {
        super(JadeThread.getMessage(m));
    }

    public DecisionException(String m, String[] param) {
        super(JadeThread.getMessage(m, param));
    }

    public DecisionException(String m, Throwable t) {
        super(JadeThread.getMessage(m), t);
    }

}
