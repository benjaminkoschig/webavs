package ch.globaz.perseus.business.exceptions.impotsource;

import ch.globaz.perseus.business.exceptions.PerseusException;

public class ImpotSourceException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ImpotSourceException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public ImpotSourceException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public ImpotSourceException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }
}
