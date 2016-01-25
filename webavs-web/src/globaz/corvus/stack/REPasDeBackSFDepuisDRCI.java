/*
 * Créé le 23 octobre 2009
 */

package globaz.corvus.stack;

import globaz.framework.utils.urls.FWSmartUrl;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

public class REPasDeBackSFDepuisDRCI implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void applyOn(FWUrlsStack stack) {
        FWSmartUrl topUrl = new FWSmartUrl(stack.peek());// peek donne l'url
        // mais sans
        // l'enlever de la
        // pile
        if ("corvus.ci.demandesRassemblement.afficher".equals(topUrl.getUserAction())) {
            for (int i = 2; i < stack.size(); i++) {
                FWSmartUrl currentUrl = new FWSmartUrl(stack.peekAt(-i));
                String userAction = currentUrl.getUserAction();
                if ("corvus.demandes.saisieDemandeRente.afficher".equals(userAction)) {
                    FWUrl top = stack.pop();// Retire
                    // virer toutes les actions y.c. et jusqu'à la currentUrl
                    // chopper l'url du dessus
                    FWUrl deletedUrl = stack.pop();// Retire
                    // tant que l'URL qu'on vient d'enlever n'est pas la
                    // currentAction...
                    while (deletedUrl != currentUrl.getFWUrl()) {
                        deletedUrl = stack.pop();// Retire
                    }
                    // remettre la top
                    stack.push(deletedUrl);// ADD
                    stack.push(top);// ADD
                    break;
                }
            }
        }
    }

    @Override
    public byte getPriority() {
        return (byte) 60;
    }

    @Override
    public String toString() {
        return "Rentes, évite de revenir en sf depuis l'écran des demandes de RCI";
    }
}
