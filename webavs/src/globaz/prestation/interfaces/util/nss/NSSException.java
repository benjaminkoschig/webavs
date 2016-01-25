package globaz.prestation.interfaces.util.nss;

import globaz.prestation.tools.PRNestedException;

/**
 * Descpription
 * 
 * @author scr Date de cr�ation 27 sept. 05
 */
public class NSSException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr�e une nouvelle instance de la classe NSSException.
     */
    public NSSException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe NSSException.
     * 
     * @param arg0
     */
    public NSSException(String arg0) {
        super(arg0);
    }

    /**
     * Cr�e une nouvelle instance de la classe NSSException.
     * 
     * @param msg
     * @param cause
     */
    public NSSException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
