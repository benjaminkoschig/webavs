package globaz.hercule.stack.rules;

import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.rules.FWExecutable;
import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.rules.execs.FWDefaultExec;

public class NaviRules extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de NaviRules
     */
    public NaviRules(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();

        FWEvaluable evaluable = new CEEvaluable(stack);
        FWExecutable executable = new FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
