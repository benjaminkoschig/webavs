/*
 * Créé le 19 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.tools;

import globaz.globall.util.JAException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Avec la version 1.4 de java est apparue la possibilité de wrapper une exception dans une autre. Cette fonctionnalité
 * est très pratique pour relancer une exception en gardant une trace de celles qui l'ont précédé.
 * </p>
 * 
 * <p>
 * Cette classe d'exception est une version maison de la classe Exception de Java 1.4.
 * </p>
 * 
 * @author vre
 */
public class SFNestedException extends JAException {

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
    public SFNestedException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe PRNestedException.
     * 
     * @param msg
     */
    public SFNestedException(String msg) {
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
    public SFNestedException(String msg, Throwable cause) {
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
