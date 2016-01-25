/*
 * Créé le 7 juin 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.stack;

import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

/**
 * @author hpe
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class IJRuleAjoutMultiEcran implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.utils.urls.rules.FWUrlsStackRule#applyOn(globaz.framework .utils.urls.FWUrlsStack)
     */
    @Override
    public void applyOn(FWUrlsStack aStack) {
        if (aStack.size() < 1) {
            return;
        }
        // si la drenière URL > useraCTION = ij.prononces.requerant.afficher
        FWUrl lastUrl = aStack.peek();
        FWParamString lastUserAction = lastUrl.getParam("userAction");
        FWParamString method = lastUrl.getParam("_method");
        if (method == null) {
            return;
        }
        if ("ij.prononces.requerant.afficher".equals(lastUserAction.getValue())) {
            if ("add".equals(method.getValue())) {
                // aStack.pop();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.utils.urls.rules.FWUrlsStackRule#getPriority()
     */
    @Override
    public byte getPriority() {
        // TODO Raccord de méthode auto-généré
        return 100;
    }

}
