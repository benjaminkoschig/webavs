package globaz.tucana.stack.rules;

import globaz.framework.utils.rules.FWExecutable;
import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.rules.execs.FWDefaultExec;
import globaz.tucana.application.TUApplication;

/**
 * Surcharge des règles par défaut au niveau du menu
 * 
 * @author fgo
 * 
 */
public class NaviRules extends FWRule {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur permettant la définition de l'action choisir
     * 
     * @param stack
     */
    public NaviRules(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();

        FWDefaultEval evaluable = new FWDefaultEval(stack, TUApplication.DEFAULT_APPLICATION_TUCANA);
        evaluable.addValidActions(new String[] { "choisir" });
        FWExecutable executable = new FWDefaultExec(stack);

        addEval(evaluable);
        addExec(executable);
    }
}
