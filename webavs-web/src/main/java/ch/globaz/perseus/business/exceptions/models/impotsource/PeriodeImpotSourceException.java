package ch.globaz.perseus.business.exceptions.models.impotsource;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * 
 * @author PCA
 * 
 */

public class PeriodeImpotSourceException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PeriodeImpotSourceException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public PeriodeImpotSourceException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public PeriodeImpotSourceException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
