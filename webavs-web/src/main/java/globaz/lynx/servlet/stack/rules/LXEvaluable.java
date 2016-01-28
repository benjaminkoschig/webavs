package globaz.lynx.servlet.stack.rules;

import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.lynx.application.LXApplication;

public class LXEvaluable extends FWDefaultEval {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LXEvaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, LXApplication.DEFAULT_APPLICATION_LYNX);
    }

}
