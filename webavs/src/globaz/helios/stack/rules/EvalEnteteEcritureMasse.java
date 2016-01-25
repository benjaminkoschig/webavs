package globaz.helios.stack.rules;

import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.rules.FWEvaluable;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * Pour tester si les URL's précédentes sont: 0: helios.comptes.enteteEcriture.afficher -1:
 * helios.comptes.enteteEcriture.afficher
 * 
 * @author vch
 */
public class EvalEnteteEcritureMasse extends FWEvaluable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWUrlsStack m_stack = null;

    public EvalEnteteEcritureMasse(FWUrlsStack aStack) {
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
        String[] userActions = { "helios.comptes.enteteEcriture.afficher", "helios.comptes.enteteEcriture.ajouter",
                "helios.comptes.enteteEcriture.afficher" };
        for (int i = 0; i < userActions.length && result; i++) {
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
