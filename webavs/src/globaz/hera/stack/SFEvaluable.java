/*
 * Créé le 1 novembre 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.stack;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.hera.application.SFApplication;
import globaz.hera.servlet.ISFActions;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author mmu
 */
public class SFEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final HashSet ACTIONS_A_EMPILER = new HashSet();

    static {
        // ACTIONS_A_EMPILER.add(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".entrerApplication");
        SFEvaluable.ACTIONS_A_EMPILER.add(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".selectionnerRequerant");
        SFEvaluable.ACTIONS_A_EMPILER.add(ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".changerDomaineRequerant");
        SFEvaluable.ACTIONS_A_EMPILER.add(ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleMembre");
        SFEvaluable.ACTIONS_A_EMPILER.add(ISFActions.ACTION_VUE_GLOBALE + ".afficherFamilleRequerant");
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWUrlsStack stackRefCopy;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe SFEvaluable.
     * 
     * @param stack
     */
    public SFEvaluable(FWUrlsStack stack) {
        super(stack, SFApplication.DEFAULT_APPLICATION_SF);
        addValidActions(new String[] { "entrerApplication" });
        addValidActions(new String[] { "selectionnerRequerant" });
        addValidActions(new String[] { "changerDomaineRequerant" });
        addValidActions(new String[] { "afficherFamilleMembre" });
        addValidActions(new String[] { "afficherFamilleRequerant" });

        stackRefCopy = stack;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param aSFion
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.utils.rules.evals.FWDefaultEval#evalApplicationASFion(globaz.framework.controller.FWASFion)
     */
    @Override
    public boolean evalApplicationAction(FWAction action) {
        if (stackRefCopy.size() <= 1) {
            return false;
        } else {
            // return !ACTIONS_A_EMPILER.equals(action.toString());

            if ((SFEvaluable.ACTIONS_A_EMPILER.contains(action.toString()) || action.toString().endsWith("afficher")
                    || action.toString().endsWith("chercher") || action.toString().endsWith("afficherFamilleRequerant"))) {
                return false;
            } else {
                return true;
            }
        }
    }
}
