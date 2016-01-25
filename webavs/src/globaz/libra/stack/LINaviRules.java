/*
 * Créé le 22 juillet 2009
 */

package globaz.libra.stack;

import globaz.framework.utils.rules.FWRule;
import globaz.framework.utils.rules.execs.FWDefaultExec;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Prepares the url stack pushing validation system.
 * </p>
 * 
 * @author hpe
 */
public class LINaviRules extends FWRule {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe LINaviRules.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public LINaviRules(FWUrlsStack stack) {
        addEval(new LIEvaluable(stack));
        addExec(new FWDefaultExec(stack));
    }
}
