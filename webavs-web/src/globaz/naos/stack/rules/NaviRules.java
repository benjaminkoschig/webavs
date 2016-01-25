package globaz.naos.stack.rules;

import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.rules.FWExecutable;
import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.rules.execs.FWDefaultExec;
import globaz.framework.utils.urls.FWUrlsStack;

public class NaviRules extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Rules de navigation pour l'affiliation.
     */
    public NaviRules(FWUrlsStack stack, String applicationId) {
        super();

        FWEvaluable evaluable = new Evaluable(stack, applicationId);
        FWExecutable executable = new FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
