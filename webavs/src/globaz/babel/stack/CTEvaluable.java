/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.stack;

import globaz.babel.application.CTApplication;
import globaz.babel.servlet.CTMainServletAction;
import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class CTEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {
        ACTIONS_A_EMPILER.add(CTMainServletAction.ACTION_DOCUMENTS + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(CTMainServletAction.ACTION_TEXTES + "." + FWAction.ACTION_CHERCHER);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe CTEvaluable.
     * 
     * @param stack
     */
    public CTEvaluable(FWUrlsStack stack) {
        super(stack, CTApplication.DEFAULT_APPLICATION_BABEL);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationAction(globaz.framework.controller.FWAction)
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {
        return !ACTIONS_A_EMPILER.contains(action.toString());
    }
}
