package globaz.naos.stack.rules;

import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * Pour tester si les URL's précédentes sont: 0: naos.cotisation.chercher -1: naos.plan.lister -2: naos.plan.chercher
 * 
 * @author vch
 */
public class EvalPlanCapage3Pages extends FWEvaluable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWUrlsStack m_stack = null;

    public EvalPlanCapage3Pages(FWUrlsStack aStack) {
        m_stack = aStack;
    }

    /**
     * @see globaz.framework.utils.rules.FWEvaluable#eval()
     */
    @Override
    protected boolean eval() {
        if (m_stack == null) {
            return false;
        }
        if (m_stack.size() < 4) {
            return false;
        }

        boolean result = true;
        int lastUrlIndex = m_stack.size() - 1;
        // String[] userActions = {"naos.cotisation.chercher",
        // "naos.plan.lister", "naos.plan.chercher"};
        String[] userActions = { "naos.plan.plan.afficher", "naos.plan.plan.lister", "naos.plan.plan.chercher" };
        for (int i = 0; (i < userActions.length) && result; i++) {
            FWUrl tmpUrl = m_stack.peekAt(lastUrlIndex - i);
            if (tmpUrl == null) {
                result = false;
                continue;
            }
            FWParam param = tmpUrl.getParam("userAction");
            if (param == null) {
                result = false;
                continue;
            }
            if (!userActions[i].equalsIgnoreCase((String) param.getValue())) {
                result = false;
                continue;
            }
        }
        return result;
    }

}
