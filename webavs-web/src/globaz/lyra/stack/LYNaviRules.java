/*
 * Créé le 25 septembre 2006
 */

package globaz.lyra.stack;

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
public class LYNaviRules extends FWRule {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -7505012904456454514L;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APNaviRules.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public LYNaviRules(FWUrlsStack stack) {
        addEval(new LYEvaluable(stack));
        addExec(new FWDefaultExec(stack));
    }
}
