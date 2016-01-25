package globaz.apg.module.calcul;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lancée lorsque le calcul a échoué POUR UNE RAISON METIER !!!!
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
     * Crée une nouvelle instance de la classe APCalculException.
     */
    public APCalculException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APCalculException.
     * 
     * @param msg
     *            DOCUMENT ME!
     */
    public APCalculException(String msg) {
        super(msg);
    }

    /**
     * Crée une nouvelle instance de la classe APCalculException.
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
