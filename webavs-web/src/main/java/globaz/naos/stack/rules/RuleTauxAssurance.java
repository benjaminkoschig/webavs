package globaz.naos.stack.rules;

import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * Règle pour le problème de la sélection du plan (naos) après nouvelle affiliation.
 * 
 * @author user
 */
public class RuleTauxAssurance extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RuleTauxAssurance(FWUrlsStack stack) {
        super();
        addEval(new EvalTauxAssurance(stack));
        addExec(new ExecTauxAssurance(stack));
        setRunMode(FWRule.MODE_RUN_WHILE_TRUE);
    }
}
