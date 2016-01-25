package globaz.lynx.servlet.stack.rules;

import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.rules.FWExecutable;
import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.rules.execs.FWDefaultExec;
import globaz.framework.utils.urls.FWUrlsStack;

public class LXNaviRules extends FWRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXNaviRules(FWUrlsStack stack) {
        super();

        FWEvaluable evaluable = new LXEvaluable(stack);
        FWExecutable executable = new FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
