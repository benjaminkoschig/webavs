/*
 * Cr�� le 19 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.prestation.acor;

import globaz.prestation.tools.PRNestedException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une exception lanc�e lorsque la g�n�ration des fichiers ACOR a pos� un probl�me. L'exception �tant la cause de
 * celle-ci est nich�e en tant qu'attribute cause.
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
     * Cr�e une nouvelle instance de la classe PRACORException.
     */
    public PRACORException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     */
    public PRACORException(String msg) {
        super(msg);
    }

    /**
     * Cr�e une nouvelle instance de la classe PRACORException.
     * 
     * @param msg
     * @param cause
     *            DOCUMENT ME!
     */
    public PRACORException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
