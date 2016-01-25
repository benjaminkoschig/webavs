package globaz.corvus.regles;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lancée lorsque une règle ne passe pas les conditions requises.
 * </p>
 * 
 * @author scr
 */

public class REReglesException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe PRACORException.
     */
    public REReglesException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     */
    public REReglesException(String msg) {
        super(msg);
    }

    /**
     * Crée une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     * @param cause
     *            DOCUMENT ME!
     */
    public REReglesException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
