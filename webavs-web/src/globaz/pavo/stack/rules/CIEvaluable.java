package globaz.pavo.stack.rules;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;

/**
 * Insérez la description du type ici. Date de création : (03.02.2003 14:53:55)
 * 
 * @author: David Girardin
 */
public class CIEvaluable extends FWDefaultEval {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur CIEvaluable.
     * 
     * @param stack
     *            globaz.framework.utils.urls.FWUrlsStack
     */
    public CIEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, globaz.pavo.application.CIApplication.DEFAULT_APPLICATION_PAVO);
        addValidActions(new String[] { "comptabiliser", "imprimer" });
    }

    @Override
    public boolean evalApplicationAction(FWAction fwAction) {
        if (fwAction.getClassPart().equals("revenuSplitting") || fwAction.getClassPart().equals("domicileSplitting")) {
            if (fwAction.getActionPart().indexOf("chercher") == -1) {
                return true;
            }
        } else if (fwAction.getClassPart().equals("ecriture")) {
            if (fwAction.getActionPart().indexOf("chercher") == -1 && !fwAction.getActionPart().equals("afficher")) {
                return true;
            }
        }
        return false;
    }
}
