/**
 * 
 */
package ch.globaz.perseus.business.exceptions.paiement;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class PaiementException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PaiementException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public PaiementException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public PaiementException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
