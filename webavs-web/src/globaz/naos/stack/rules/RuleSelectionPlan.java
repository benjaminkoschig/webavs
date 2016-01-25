package globaz.naos.stack.rules;

import globaz.framework.utils.rules.FWRule;

/**
 * R�gle pour le probl�me de la s�lection du plan (naos) apr�s nouvelle affiliation.
 * 
 * @author user
 */
public class RuleSelectionPlan extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RuleSelectionPlan(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();
        addEval(new EvalSelectionPlan(stack));
        addExec(new ExecSelectionPlan(stack));
        setRunMode(FWRule.MODE_RUN_WHILE_TRUE);
    }

}
