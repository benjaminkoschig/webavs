package globaz.campus.stack.rules;

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
     * Commentaire relatif au constructeur SupprimeListe.
     */
    public NaviRules(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();

        FWEvaluable evaluable = new Evaluable(stack);
        FWExecutable executable = new FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
