package globaz.corvus.api.external.arc;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lancée lorsque le transfer des resultats des ARC a posé un problème. L'exception étant la cause de
 * celle-ci est nichée en tant qu'attribute cause.
 * </p>
 * 
 * @author scr
 */
public class REDownloaderException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe REUploaderException.
     */
    public REDownloaderException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe REUploaderException.
     * 
     * @param msg
     */
    public REDownloaderException(String msg) {
        super(msg);
    }

    /**
     * Crée une nouvelle instance de la classe REUploaderException.
     * 
     * @param msg
     * @param cause
     *            DOCUMENT ME!
     */
    public REDownloaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
