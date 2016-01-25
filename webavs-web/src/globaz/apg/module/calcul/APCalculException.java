package globaz.apg.module.calcul;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lanc�e lorsque le calcul a �chou� POUR UNE RAISON METIER !!!!
 * </p>
 * 
 * @author vre
 */
public class APCalculException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr�e une nouvelle instance de la classe APCalculException.
     */
    public APCalculException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe APCalculException.
     * 
     * @param msg
     *            DOCUMENT ME!
     */
    public APCalculException(String msg) {
        super(msg);
    }

    /**
     * Cr�e une nouvelle instance de la classe APCalculException.
     * 
     * @param msg
     *            DOCUMENT ME!
     * @param cause
     *            DOCUMENT ME!
     */
    public APCalculException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
