package globaz.helios.stack.rules;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;

/**
 * Insérez la description du type ici. Date de création : (03.02.2003 14:53:55)
 * 
 * @author: David Girardin
 */
public class CGEvaluable extends FWDefaultEval {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CGyEvaluable.
     * 
     * @param stack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    public CGEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, globaz.helios.application.CGApplication.DEFAULT_APPLICATION_HELIOS);
        addValidActions(new String[] { "imprimerBalanceComptes", "imprimer" });
    }

    @Override
    public boolean evalApplicationAction(FWAction fwAction) {

        if (fwAction.getClassPart().equals("ecritureCollective") && fwAction.getActionPart().indexOf("chercher") == -1) {
            return true;
        }
        return false;
    }
}
