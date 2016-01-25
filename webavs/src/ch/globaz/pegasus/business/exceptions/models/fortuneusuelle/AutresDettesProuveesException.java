package ch.globaz.pegasus.business.exceptions.models.fortuneusuelle;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AutresDettesProuveesException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AutresDettesProuveesException() {
        super();
    }

    /**
     * @param m
     */
    public AutresDettesProuveesException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public AutresDettesProuveesException(String m, Throwable t) {
        super(m, t);
    }
}