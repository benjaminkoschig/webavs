/*
 * Créé le 1 novembre 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.stack;

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
 * @author mmu
 */
public class SFNaviRules extends FWRule {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe APNaviRules.
     * 
     * @param stack
     *            DOCUMENT ME!
     */
    public SFNaviRules(FWUrlsStack stack) {
        addEval(new SFEvaluable(stack));
        addExec(new FWDefaultExec(stack));
    }
}
