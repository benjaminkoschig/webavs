/*
 * Cr�� le 6 mars 08
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.campus.stack.rules;

import globaz.campus.application.GEApplication;
import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */
public class GEEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr�e une nouvelle instance de la classe APEvaluable.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public GEEvaluable(FWUrlsStack stack) {
        super(stack, GEApplication.DEFAULT_APPLICATION_CAMPUS);
    }

    /**
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationAction(globaz.framework.controller.FWAction)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {

        if (action.toString().endsWith("afficher") || action.toString().endsWith("chercher")) {
            return false;
        } else {
            return true;
        }

    }

}
