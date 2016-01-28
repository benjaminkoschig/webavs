package ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AssuranceRenteViagereException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AssuranceRenteViagereException() {
        super();
    }

    /**
     * @param m
     */
    public AssuranceRenteViagereException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AssuranceRenteViagereException(String m, Throwable t) {
        super(m, t);
    }

}
