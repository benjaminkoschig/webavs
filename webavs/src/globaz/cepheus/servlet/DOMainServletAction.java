/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.servlet;

import globaz.framework.controller.FWAction;
import globaz.jade.log.JadeLogger;
import java.util.HashMap;

/**
 * DOCUMENT ME!
 * 
 * @author scr
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class DOMainServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final HashMap ACTIONS = new HashMap();

    static {
        ACTIONS.put(IDOActions.ACTION_CHERCHER_DEMANDE_PRESTATIONS, DODemandePrestationsAction.class);
        ACTIONS.put(IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS, DOMetaDossierJointIntervenantsAction.class);
        ACTIONS.put(IDOActions.ACTION_CHERCHER_TAUX_IMPOSITIONS, DOTauxImpositionAction.class);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut action class
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut action class
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static Class getActionClass(FWAction action) throws Exception {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();
        Class c = (Class) ACTIONS.get(key);

        if (c == null) {
            JadeLogger.warn(DOMainServletAction.class, "CEPHEUS : No Class found to match action : " + key);
        }

        return c;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APMainServletAction.
     */
    public DOMainServletAction() {
    }
}
