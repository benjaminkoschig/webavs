/*
 * Cr�� le 19 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Identique � PRNestedException mais cette exception-ci etend RuntimeException, avec tout ce que cela comporte...
 * </p>
 * 
 * @author vre
 */
public class PRNestedRuntimeException extends RuntimeException {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Throwable cause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe PRNestedException.
     */
    public PRNestedRuntimeException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe PRNestedException.
     * 
     * @param msg
     */
    public PRNestedRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Cr�e une nouvelle instance de la classe PRNestedException.
     * 
     * @param msg
     *            le message de l'exception.
     * @param cause
     *            l'exception qui a caus� celle-ci.
     */
    public PRNestedRuntimeException(String msg, Throwable cause) {
        this(msg);
        this.cause = cause;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    @Override
    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);

        if (cause != null) {
            out.println();
            out.println("caused by: " + cause.getMessage());
            cause.printStackTrace(out);
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    @Override
    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);

        if (cause != null) {
            out.println();
            out.println("caused by: " + cause.getMessage());
            cause.printStackTrace(out);
        }
    }
}
