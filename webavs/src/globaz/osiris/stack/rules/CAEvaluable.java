package globaz.osiris.stack.rules;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;

/**
 * Insérez la description du type ici. Date de création : (08.01.2004 14:53:55)
 * 
 * @author: Sébastien Chappatte
 */
public class CAEvaluable extends FWDefaultEval {
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
    public CAEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, globaz.osiris.application.CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    @Override
    public boolean evalApplicationAction(FWAction action) {

        if (action.getClassPart().equals("detailInteretMoratoire")) {
            if (action.getActionPart().indexOf("chercher") == -1) {
                return true;
            }
        }

        if (action.getClassPart().equals("retours")) {
            if ((action.getActionPart().indexOf("actionListerLignesRetoursSurSection") != -1)
                    || (action.getActionPart().indexOf("actionListerLignesRetoursSurAdressePaiement") != -1)) {
                return true;
            }
        }

        if ((action.getClassPart().equals("journalOperationEcriture"))) {
            /*
             * if (action.getActionPart().indexOf("chercher") == -1 && !action.getActionPart().equals("afficher")) {
             * return true; }
             */
        }
        if ((action.getClassPart().equals("referenceRubrique"))) {
            if ((action.getActionPart().indexOf("chercher") == -1) && !action.getActionPart().equals("afficher")) {
                return true;
            }
        }
        return false;
    }
}
