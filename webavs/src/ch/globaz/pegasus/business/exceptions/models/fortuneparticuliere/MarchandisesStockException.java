package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class MarchandisesStockException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public MarchandisesStockException() {
        super();
    }

    /**
     * @param m
     */
    public MarchandisesStockException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public MarchandisesStockException(String m, Throwable t) {
        super(m, t);
    }

}
