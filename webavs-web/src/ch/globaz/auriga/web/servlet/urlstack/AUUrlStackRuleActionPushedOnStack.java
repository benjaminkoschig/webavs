package ch.globaz.auriga.web.servlet.urlstack;

import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;
import java.util.List;

public class AUUrlStackRuleActionPushedOnStack implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static String PARAM_USER_ACTION = "userAction";

    private List<String> actionPushedOnStack = null;

    public AUUrlStackRuleActionPushedOnStack(List<String> actionPushedOnStack) {
        this.actionPushedOnStack = actionPushedOnStack;
    }

    @Override
    public void applyOn(FWUrlsStack aStack) {

        if (aStack.size() >= 1) {
            FWUrl lastUrl = aStack.peek();
            FWParam lastUrlUserAction = lastUrl.getParam(AUUrlStackRuleActionPushedOnStack.PARAM_USER_ACTION);
            String usrAction = (String) lastUrlUserAction.getValue();
            if (!actionPushedOnStack.contains(usrAction) && usrAction.startsWith("auriga.")) {
                aStack.pop();
            }
        }
    }

    public List<String> getActionPushedOnStack() {
        return actionPushedOnStack;
    }

    @Override
    public byte getPriority() {
        return 127;
    }

    public void setActionPushedOnStack(List<String> actionPushedOnStack) {
        this.actionPushedOnStack = actionPushedOnStack;
    }

}
