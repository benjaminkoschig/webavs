package globaz.corvus.api.external.arc;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lanc?e lorsque le transfer des resultats des ARC a pos? un probl?me. L'exception ?tant la cause de
 * celle-ci est nich?e en tant qu'attribute cause.
 * </p>
 * 
 * @author scr
 */
public class REUploaderException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr?e une nouvelle instance de la classe REUploaderException.
     */
    public REUploaderException() {
        super();
    }

    /**
     * Cr?e une nouvelle instance de la classe REUploaderException.
     * 
     * @param msg
     */
    public REUploaderException(String msg) {
        super(msg);
    }

    /**
     * Cr?e une nouvelle instance de la classe REUploaderException.
     * 
     * @param msg
     * @param cause
     *            DOCUMENT ME!
     */
    public REUploaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
