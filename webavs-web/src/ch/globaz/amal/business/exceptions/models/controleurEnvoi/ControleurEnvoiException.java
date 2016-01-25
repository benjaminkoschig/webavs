/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.controleurEnvoi;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author dhi
 * 
 */
public class ControleurEnvoiException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ControleurEnvoiException() {
    }

    /**
     * @param m
     */
    public ControleurEnvoiException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public ControleurEnvoiException(String m, Throwable t) {
        super(m, t);
    }

}
