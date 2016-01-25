package globaz.naos.stack.rules;

import globaz.framework.utils.rules.FWRule;

/**
 * R�gle pour le probl�me de la s�lection du plan (naos) apr�s nouvelle affiliation.
 * 
 * @author user
 */
public class RulePlanCapage3Pages extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RulePlanCapage3Pages(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();
        addEval(new EvalPlanCapage3Pages(stack));
        addExec(new ExecPlanCapage3Pages(stack));
        setRunMode(FWRule.MODE_RUN_WHILE_TRUE);
    }

}
