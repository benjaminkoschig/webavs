/*
 * Créé le 25 septembre 2006
 */

package globaz.lyra.stack;

import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.lyra.application.LYApplication;
import globaz.lyra.servlet.ILYActions;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */
public class LYEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    private static final long serialVersionUID = 6924389415288509909L;

    static {
        ACTIONS_A_EMPILER.add(FWServlet.BACK);
        ACTIONS_A_EMPILER.add(ILYActions.ACTION_ECHEANCES + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(ILYActions.ACTION_HISTORIQUE + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(ILYActions.ACTION_PARAMETRES + "." + FWAction.ACTION_CHERCHER);
        ACTIONS_A_EMPILER.add(ILYActions.ACTION_PREPARATION + "." + FWAction.ACTION_AFFICHER);
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWUrlsStack stackRefCopy;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFEvaluable.
     * 
     * @param stack
     */
    public LYEvaluable(FWUrlsStack stack) {
        super(stack, LYApplication.DEFAULT_APPLICATION_LYRA);
        addValidActions(new String[] { "" });
        stackRefCopy = stack;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param aSFion
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationASFion(globaz.framework.controller.FWASFion)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {
        if (stackRefCopy.size() <= 1) {
            return false;
        } else {

            if ((ACTIONS_A_EMPILER.contains(action.toString()) || action.toString().endsWith("chercher") || action
                    .toString().endsWith("preparation.afficher"))
                    // supprimer de la pile toutes les useraction suivantes
                    && (!action.toString().endsWith("reAfficher"))
                    && (!action.toString().endsWith("lister"))
                    && (!action.toString().endsWith("apercuParametres.afficher"))
                    && (!action.toString().endsWith("apercuEcheances.afficher"))) {
                return false;
            } else {
                return true;
            }
        }
    }
}
