/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.controleurJob;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author dhi
 * 
 */
public class ControleurJobException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ControleurJobException() {
    }

    /**
     * @param m
     */
    public ControleurJobException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public ControleurJobException(String m, Throwable t) {
        super(m, t);
    }

}
