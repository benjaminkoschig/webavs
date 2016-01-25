package globaz.helios.stack.rules;

import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * R�gle pour le probl�me de la s�lection du plan (vela) apr�s nouvelle affiliation.
 * 
 * @author user
 */
public class RuleEnteteEcritureMasse extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RuleEnteteEcritureMasse(FWUrlsStack stack) {
        super();
        addEval(new EvalEnteteEcritureMasse(stack));
        addExec(new ExecEnteteEcritureMasse(stack));
        setRunMode(FWRule.MODE_RUN_WHILE_TRUE);
    }

}
