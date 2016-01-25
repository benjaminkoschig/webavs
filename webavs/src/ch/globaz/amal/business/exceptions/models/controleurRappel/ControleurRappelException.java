/**
 * 
 */
package ch.globaz.amal.business.exceptions.models.controleurRappel;

import ch.globaz.amal.business.exceptions.AmalException;

/**
 * @author dhi
 * 
 */
public class ControleurRappelException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ControleurRappelException() {
    }

    /**
     * @param m
     */
    public ControleurRappelException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public ControleurRappelException(String m, Throwable t) {
        super(m, t);
    }

}
