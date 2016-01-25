package globaz.phenix.stack.rules;

import globaz.framework.utils.urls.FWSmartUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

public class CPDecisionValiderRule implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void applyOn(FWUrlsStack aStack) {
        if (aStack.size() < 2) {
            return;
        }
        FWSmartUrl lastUrl = new FWSmartUrl(aStack.peek());
        FWSmartUrl preLastUrl = new FWSmartUrl(aStack.peekAt(aStack.size() - 2));
        if ("phenix.principale.decisionValider.afficher".equals(lastUrl.getUserAction())) {
            if (!"phenix.communications.validationJournalRetour.valider".equals(preLastUrl.getUserAction())
                    && !"phenix.communications.validationJournalRetour.chercher".equals(preLastUrl.getUserAction())) {
                // aStack.pop();
            }
        }
    }

    @Override
    public byte getPriority() {
        return 120;
    }

    @Override
    public String toString() {
        return "Enlève [phenix.principale.decisionValider.afficher] si précédé de [phenix.communications.validationJournalRetour.valider]";
    }
}
