package globaz.corvus.dao;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lanc�e lorsque la tentative de suppression d'une d�cision valid�e
 * </p>
 * 
 * @author SCR
 */
public class REAttemptDeletionDecisionValidateException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr�e une nouvelle instance de la classe PRACORException.
     */
    public REAttemptDeletionDecisionValidateException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     */
    public REAttemptDeletionDecisionValidateException(String msg) {
        super(msg);
    }

    /**
     * Cr�e une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     * @param cause
     *            DOCUMENT ME!
     */
    public REAttemptDeletionDecisionValidateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
