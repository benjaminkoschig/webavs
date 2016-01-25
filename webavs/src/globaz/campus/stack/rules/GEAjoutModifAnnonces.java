package globaz.campus.stack.rules;

import globaz.campus.application.GEApplication;
import globaz.framework.controller.FWAction;
import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

public class GEAjoutModifAnnonces implements FWUrlsStackRule {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String PARAM_USER_ACTION = "userAction";

    @Override
    public void applyOn(FWUrlsStack stack) {
        if (stack.size() >= 1) {
            FWUrl lastUrl = stack.peek();
            FWParam lastUrlUserAction = lastUrl.getParam(PARAM_USER_ACTION);
            String usrAction = (String) lastUrlUserAction.getValue();
            FWAction _monAction = FWAction.newInstance(usrAction);
            if (GEApplication.DEFAULT_APPLICATION_CAMPUS.equalsIgnoreCase(_monAction.getApplicationPart())) {
                // si la classe de la userAction chercher/afficher
                if (!usrAction.endsWith(FWAction.ACTION_CHERCHER) && !usrAction.endsWith(FWAction.ACTION_AFFICHER)) {
                    lastUrl = stack.pop();
                }
            }
        }

        // if (stack.size() < 3) {
        // return;
        // }
        // FWSmartUrl topUrl = new FWSmartUrl(stack.peekAt(stack.size() - 1));
        // FWSmartUrl midUrl = new FWSmartUrl(stack.peekAt(stack.size() - 2));
        // FWSmartUrl lowUrl = new FWSmartUrl(stack.peekAt(stack.size() - 3));
        //
        // if
        // ("campus.annonces.annonces.afficher".equals(topUrl.getUserAction())
        // ||
        // "campus.annonces.imputations.afficher".equals(topUrl.getUserAction()))
        // {
        // if
        // ("campus.annonces.annonces.modifier".equals(midUrl.getUserAction())
        // ||
        // "campus.annonces.imputations.modifier".equals(midUrl.getUserAction())
        // || "campus.annonces.annonces.ajouter".equals(midUrl.getUserAction())
        // ||
        // "campus.annonces.imputations.ajouter".equals(midUrl.getUserAction()))
        // {
        // if
        // ("campus.annonces.annonces.afficher".equals(lowUrl.getUserAction())
        // ||
        // "campus.annonces.imputations.afficher".equals(lowUrl.getUserAction()))
        // {
        // FWUrl top = stack.pop();
        // stack.pop();
        // stack.pop();
        // stack.push(top);
        // }
        // }
        // }
    }

    @Override
    public byte getPriority() {
        return 100;
    }
}
