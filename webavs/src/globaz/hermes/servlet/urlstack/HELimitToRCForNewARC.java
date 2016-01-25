package globaz.hermes.servlet.urlstack;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;
import globaz.hermes.application.HEApplication;
import globaz.hermes.servlet.HEMainServlet;

public class HELimitToRCForNewARC implements FWUrlsStackRule {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String PARAM_USER_ACTION = "userAction";

    @Override
    public void applyOn(FWUrlsStack aStack) {
        if (aStack.size() >= 1) {
            FWUrl lastUrl = aStack.peek();
            FWParam lastUrlUserAction = lastUrl.getParam(PARAM_USER_ACTION);
            String usrAction = (String) lastUrlUserAction.getValue();
            FWAction _monAction = FWAction.newInstance(usrAction);
            if (HEApplication.DEFAULT_APPLICATION_HERMES.equalsIgnoreCase(_monAction.getApplicationPart())) {
                // si la classe de la userAction est de type INPUTANNONCE, on ne
                // garde que les action chercher
                if ((HEMainServlet.CLASS_INPUTANNONCE.equals(_monAction.getClassPart()) || HEMainServlet.CLASS_ANNONCEORPHELINES
                        .equals(_monAction.getClassPart())) && !usrAction.endsWith(FWAction.ACTION_CHERCHER)) {
                    lastUrl = aStack.pop();
                } else if (!usrAction.endsWith(FWAction.ACTION_CHERCHER)
                        && !usrAction.endsWith(FWAction.ACTION_AFFICHER)) {
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
        return "Limits the URL's stack to rc for insert news ARC";
    }

}
