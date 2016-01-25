package globaz.osiris.stack.rules;

/**
 * Cette classe permet les règles de navigation Date de création : (06.09.2002 09:22:30)
 * 
 * @author: Administrator
 */
import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.rules.FWExecutable;
import globaz.framework.utils.rules.FWRule;

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

        // FWEvaluable evaluable = new Evaluable(stack);
        // FWExecutable executable = new Executable(stack);

        FWEvaluable evaluable = new CAEvaluable(stack);
        FWExecutable executable = new globaz.framework.utils.rules.execs.FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
