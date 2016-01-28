/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.pcfaccordee;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author DDE
 * 
 */
public class PCFAccordeeException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PCFAccordeeException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param m
     */
    public PCFAccordeeException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public PCFAccordeeException(String m, Throwable t) {
        super(m, t);
    }

}
