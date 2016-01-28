package globaz.phenix.stack.rules;

/**
 * Insérez la description du type ici. Date de création : (06.09.2002 09:22:30)
 * 
 * @author: Administrator
 */
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
        // addEval(new CPEvaluable(stack));
        // FWEvaluable evaluable = new Evaluable(stack);
        // FWExecutable executable = new Executable(stack);

        FWEvaluable evaluable = new CPEvaluable(stack);
        FWExecutable executable = new FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
