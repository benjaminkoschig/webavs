package globaz.prestation.interfaces.util.nss;

import globaz.prestation.tools.PRNestedException;

/**
 * Descpription
 * 
 * @author scr Date de création 27 sept. 05
 */
public class NSSException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe NSSException.
     */
    public NSSException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe NSSException.
     * 
     * @param arg0
     */
    public NSSException(String arg0) {
        super(arg0);
    }

    /**
     * Crée une nouvelle instance de la classe NSSException.
     * 
     * @param msg
     * @param cause
     */
    public NSSException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
