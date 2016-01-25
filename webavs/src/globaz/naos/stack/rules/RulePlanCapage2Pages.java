package globaz.naos.stack.rules;

import globaz.framework.utils.rules.FWRule;

/**
 * Règle pour le problème de la sélection du plan (naos) après nouvelle affiliation.
 * 
 * @author user
 */
public class RulePlanCapage2Pages extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RulePlanCapage2Pages(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();
        addEval(new EvalPlanCapage2Pages(stack));
        addExec(new ExecPlanCapage2Pages(stack));
        setRunMode(FWRule.MODE_RUN_WHILE_TRUE);
    }

}
