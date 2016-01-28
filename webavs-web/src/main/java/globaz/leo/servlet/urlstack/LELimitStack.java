/*
 * Créé le 18 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.servlet.urlstack;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;
import globaz.leo.application.LEApplication;
import globaz.leo.servlet.LEMainServletAction;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LELimitStack implements FWUrlsStackRule {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String PARAM_USER_ACTION = "userAction";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.utils.urls.rules.FWUrlsStackRule#applyOn(globaz.framework .utils.urls.FWUrlsStack)
     */
    @Override
    public void applyOn(FWUrlsStack aStack) {
        if (aStack.size() >= 1) {
            FWUrl lastUrl = aStack.peek();
            FWParam lastUrlUserAction = lastUrl.getParam(PARAM_USER_ACTION);
            String usrAction = (String) lastUrlUserAction.getValue();
            FWAction _monAction = FWAction.newInstance(usrAction);
            if (LEApplication.DEFAULT_APPLICATION_LEO.equalsIgnoreCase(_monAction.getApplicationPart())) {
                // dans le module LEO on ne peut revenir que que des pages
                // chercher ou afficher.
                if (!usrAction.endsWith(FWAction.ACTION_CHERCHER) && !usrAction.endsWith(FWAction.ACTION_AFFICHER)) {
                    lastUrl = aStack.pop();
                }
                if (LEMainServletAction.CLASS_EDITER_FORMULE.equals(_monAction.getClassPart())) {
                    lastUrl = aStack.pop();
                }
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
        return 126;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Limits the URL's stack to the LEO's module";
    }
}
