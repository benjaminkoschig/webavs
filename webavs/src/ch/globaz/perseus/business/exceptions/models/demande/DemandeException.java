/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.demande;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class DemandeException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DemandeException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public DemandeException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public DemandeException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
