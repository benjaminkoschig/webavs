package globaz.draco.stack.rules;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;

/**
 * Insérez la description du type ici. Date de création : (08.01.2004 14:53:55)
 * 
 * @author: Sébastien Chappatte
 */
public class DSEvaluable extends FWDefaultEval {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur DSEvaluable.
     * 
     * @param stack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    public DSEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, globaz.draco.application.DSApplication.DEFAULT_APPLICATION_DRACO);
        addValidActions(new String[] { "imprimer" });

    }

    @Override
    public boolean evalApplicationAction(FWAction fwAction) {

        if ((fwAction.getClassPart().equals("ligneDeclaration"))) {
            if (fwAction.getActionPart().indexOf("chercher") == -1 && !fwAction.getActionPart().equals("afficher")) {
                return true;
            }
            if (fwAction.getActionPart().indexOf("chercher") == -1) {
                return true;
            }
        }
        return false;
    }
}
