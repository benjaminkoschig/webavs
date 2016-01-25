package globaz.hercule.stack.rules;

import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.hercule.application.CEApplication;

/**
 * @author: David Girardin
 * @since (03.02.2003 14:53:55)
 */
public class CEEvaluable extends FWDefaultEval {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de CEEvaluable
     */
    public CEEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, CEApplication.DEFAULT_APPLICATION_HERCULE);
        addValidActions(new String[] { "imprimer", "imprimerControle", "imprimerlettrelibre", "lettreProchainControle" });
    }
    // public boolean evalApplicationAction(FWAction fwAction) {
    // if (fwAction.getClassPart().equals("revenuSplitting")
    // || fwAction.getClassPart().equals("domicileSplitting")) {
    // if (fwAction.getActionPart().indexOf("chercher") == -1) {
    // return true;
    // }
    // } else if (fwAction.getClassPart().equals("ecriture")) {
    // if (fwAction.getActionPart().indexOf("chercher") == -1
    // && !fwAction.getActionPart().equals("afficher")) {
    // return true;
    // }
    // }
    // return false;
    // }
}
