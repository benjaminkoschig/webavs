package ch.globaz.pegasus.business.exceptions.models.droit;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DonneesPersonnellesException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DonneesPersonnellesException() {
        super();
    }

    /**
     * @param m
     */
    public DonneesPersonnellesException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public DonneesPersonnellesException(String m, Throwable t) {
        super(m, t);
    }

}
