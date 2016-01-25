/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.donneesfinancieres;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class DonneesFinancieresException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DonneesFinancieresException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public DonneesFinancieresException(String m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     * @param t
     */
    public DonneesFinancieresException(String m, Throwable t) {
        super(m, t);
        // TODO Auto-generated constructor stub
    }

}
