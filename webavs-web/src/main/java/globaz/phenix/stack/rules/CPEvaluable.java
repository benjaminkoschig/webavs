package globaz.phenix.stack.rules;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;

/**
 * Insérez la description du type ici. Date de création : (03.02.2003 14:53:55)
 * 
 * @author: David Girardin
 */
public class CPEvaluable extends FWDefaultEval {

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
    public CPEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
        addValidActions(new String[] { "imprimer", "modifierDecision" });
    }

    @Override
    public boolean evalApplicationAction(FWAction fwAction) {
        if ((fwAction.getClassPart().equals("remarqueDecision"))
                || (fwAction.getClassPart().equalsIgnoreCase("lienTypeDecRemarque"))
                || (fwAction.getClassPart().equalsIgnoreCase("commentaireRemarqueType"))) {
            if (fwAction.getActionPart().indexOf("chercher") == -1) {
                return true;
            }
        }
        return false;
    }
}
