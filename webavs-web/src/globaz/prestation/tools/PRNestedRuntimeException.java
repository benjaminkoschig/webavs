/*
 * Créé le 19 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Identique à PRNestedException mais cette exception-ci etend RuntimeException, avec tout ce que cela comporte...
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
     * Crée une nouvelle instance de la classe PRNestedException.
     */
    public PRNestedRuntimeException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe PRNestedException.
     * 
     * @param msg
     */
    public PRNestedRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Crée une nouvelle instance de la classe PRNestedException.
     * 
     * @param msg
     *            le message de l'exception.
     * @param cause
     *            l'exception qui a causé celle-ci.
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
