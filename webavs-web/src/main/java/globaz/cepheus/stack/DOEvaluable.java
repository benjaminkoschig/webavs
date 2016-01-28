/*
 * Créé le 2 novembre 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.stack;

import globaz.cepheus.application.DOApplication;
import globaz.cepheus.servlet.IDOActions;
import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class DOEvaluable extends FWDefaultEval {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // depuis l'exérieur on n'empile que l'adresse d'entrée
    private static final String ACTIONS_A_EMPILER = IDOActions.ACTION_CHERCHER_DEMANDE_PRESTATIONS + ".chercher"
            + IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS + ".chercherDepuisDemande";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWUrlsStack stackRefCopy;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe DOEvaluable.
     * 
     * @param stack
     */
    public DOEvaluable(FWUrlsStack stack) {
        super(stack, DOApplication.DEFAULT_APPLICATION_CEPHEUS);
        addValidActions(new String[] { "entrerApplication" });
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

            if ((ACTIONS_A_EMPILER.equals(action.toString()) || action.toString().endsWith("afficher") || action
                    .toString().endsWith("chercher"))
                    // ne pas garder sur la pile les useraction afficher des
                    // caPage
                    && (!action.toString().endsWith("demandePrestations.afficher"))
                    && (!action.toString().endsWith("metaDossierJointIntervenants.afficher"))
                    && (!action.toString().endsWith("tauxImposition.afficher"))
                    && (!action.toString().endsWith("tauxImposition.lister"))
                    && (!action.toString().endsWith("demandePrestations.lister"))
                    && (!action.toString().endsWith("metaDossierJointIntervenants.lister"))) {
                return false;
            } else {
                return true;
            }
        }
    }
}
