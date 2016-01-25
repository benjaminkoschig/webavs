/*
 * Créé le 19 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.acor;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lancée lorsque la génération des fichiers ACOR a posé un problème. L'exception étant la cause de
 * celle-ci est nichée en tant qu'attribute cause.
 * </p>
 * 
 * @author vre
 */
public class PRACORException extends PRNestedException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe PRACORException.
     */
    public PRACORException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     */
    public PRACORException(String msg) {
        super(msg);
    }

    /**
     * Crée une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     * @param cause
     *            DOCUMENT ME!
     */
    public PRACORException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
