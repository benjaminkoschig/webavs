/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.stack;

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
 * @author jje
 */
public class RFNaviRules extends FWRule {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe RFNaviRules.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public RFNaviRules(FWUrlsStack stack) {
        addEval(new RFEvaluable(stack));
        addExec(new FWDefaultExec(stack));
    }
}