package globaz.tucana.stack.rules;

import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;
import java.io.Serializable;

/**
 * Classe permet la définition des urls à empiler ou non
 * 
 * @author fgo
 * 
 */
public class TUGererSupprimerRule implements FWUrlsStackRule, Serializable {

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
        int maxIndex = aStack.size() - 1;
        if (maxIndex < 3) {
            return;
        }
        // Get the URL's
        FWUrl urlOne = aStack.peekAt(maxIndex);
        FWUrl urlTwo = aStack.peekAt(maxIndex - 1);
        // Get the userActions
        try {
            String userActionOne = (String) urlOne.getParam("userAction").getValue();
            String userActionTwo = (String) urlTwo.getParam("userAction").getValue();
            if (userActionTwo.endsWith(".afficher") && userActionOne.endsWith(".supprimer")) {
                int indexSupprimer = userActionOne.indexOf(".supprimer");
                int indexAfficher = userActionTwo.indexOf(".afficher");
                if (userActionOne.substring(0, indexSupprimer).equals(userActionTwo.substring(0, indexAfficher))) {
                    aStack.pop();
                    aStack.pop();
                }
            }
        } catch (NullPointerException e) {
            // Une URL n'a pas de userAction: on sort sans rien faire
            // TODO: supprimer le printStackTrace (debug)
            e.printStackTrace();
            return;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.utils.urls.rules.FWUrlsStackRule#getPriority()
     */
    @Override
    public byte getPriority() {
        return 50;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Gestion des supprimer/afficher";
    }

}
