/*
 * Cr?? le 17 ao?t 05
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.ij.stack;

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
 * @author vre
 */
public class IJNaviRules extends FWRule {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr?e une nouvelle instance de la classe APNaviRules.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public IJNaviRules(FWUrlsStack stack) {
        addEval(new IJEvaluable(stack));
        addExec(new FWDefaultExec(stack));
    }
}
