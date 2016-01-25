package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AutreFortuneMobiliereException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AutreFortuneMobiliereException() {
        super();
    }

    /**
     * @param m
     */
    public AutreFortuneMobiliereException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AutreFortuneMobiliereException(String m, Throwable t) {
        super(m, t);
    }

}
