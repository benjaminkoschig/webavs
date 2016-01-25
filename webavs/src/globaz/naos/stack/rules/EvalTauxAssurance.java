package globaz.naos.stack.rules;

import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * Pour tester si les URL's de la pile sont: 0: naos.tauxAssurance.chercher -1: naos.tauxAssurance.afficher -2:
 * naos.tauxAssurance.chercher
 * 
 * @author vch
 */
public class EvalTauxAssurance extends FWEvaluable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWUrlsStack m_stack = null;

    public EvalTauxAssurance(FWUrlsStack aStack) {
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
        if (m_stack.size() < 3) {
            return false;
        }

        boolean result = true;
        int lastUrlIndex = m_stack.size() - 1;
        // String[] userActions = {"naos.cotisation.chercher",
        // "naos.plan.lister", "naos.plan.chercher"};
        String[] userActions = { "naos.tauxAssurance.tauxAssurance.chercher",
                "naos.tauxAssurance.tauxAssurance.afficher", "naos.tauxAssurance.tauxAssurance.chercher" };
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
