package globaz.cygnus.stack;

import globaz.framework.utils.urls.FWSmartUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

/**
 * author fha
 */
public class RFGereCaPages implements FWUrlsStackRule {

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
        FWSmartUrl preLastUrl = new FWSmartUrl(aStack.peekAt(-1));
        String lastUserAction = lastUrl.getUserAction();
        String preLastUserAction = preLastUrl.getUserAction();
        // caPage des conventions
        if ("cygnus.conventions.rechercheMontantsConvention.afficher".equals(lastUserAction)
                && "cygnus.conventions.rechercheMontantsConvention.chercher".equals(preLastUserAction)) {
            aStack.pop();
        }
    }

    @Override
    public byte getPriority() {
        return -110;
    }

}
