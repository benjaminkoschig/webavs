/*
 * Créé le 28 juin 07
 */

package globaz.corvus.stack;

import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.rules.execs.FWDefaultExec;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * @author HPE
 * 
 */

public class RENaviRules extends FWRule {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe RENaviRules.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public RENaviRules(FWUrlsStack stack) {
        addEval(new REEvaluable(stack));
        addExec(new FWDefaultExec(stack));
    }

}
