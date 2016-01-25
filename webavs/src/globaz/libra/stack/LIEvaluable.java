/*
 * Créé le 22 juillet 2009
 */

package globaz.libra.stack;

import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.libra.application.LIApplication;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */
public class LIEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {
        ACTIONS_A_EMPILER.add(FWServlet.BACK);
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWUrlsStack stackRefCopy;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIEvaluable.
     * 
     * @param stack
     */
    public LIEvaluable(FWUrlsStack stack) {
        super(stack, LIApplication.DEFAULT_APPLICATION_LIBRA);
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
                    .toString().endsWith("afficher"))
            // supprimer de la pile toutes les useraction suivantes
                    && (!action.toString().endsWith("reAfficher")) && (!action.toString().endsWith("lister"))) {
                return false;
            } else {
                return true;
            }
        }
    }
}
