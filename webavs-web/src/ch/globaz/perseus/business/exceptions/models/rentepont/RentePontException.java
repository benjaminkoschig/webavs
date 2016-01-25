/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.rentepont;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class RentePontException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RentePontException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public RentePontException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public RentePontException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
