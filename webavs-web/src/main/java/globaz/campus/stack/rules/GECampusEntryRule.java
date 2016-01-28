package globaz.campus.stack.rules;

import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

public class GECampusEntryRule implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void applyOn(FWUrlsStack stack) {
        if (stack.size() < 2) {
            return;
        }
        FWUrl url = stack.peekAt(stack.size() - 2);
        FWParamString paramColonneSelection = url.getParam("colonneSelection");
        if (paramColonneSelection != null && paramColonneSelection.getValue().equals("yes")) {
            FWUrl lastUrl = stack.pop();
            stack.pop();
            stack.push(lastUrl);
        }
        /*
         * if (stack.size() < 3) { return; } FWUrl url = stack.peek(); FWSmartUrl sUrl = new FWSmartUrl(url); FWAction
         * action = FWAction.newInstance(sUrl.getUserAction()); if (action.getApplicationPart().startsWith("campus")) {
         * boolean hasOtherApps = false; for (int i = 0; i < stack.size(); i++) { hasOtherApps |=
         * !((String)(stack.peekAt(i).getParam("userAction").getValue ())).startsWith("campus"); if (hasOtherApps) {
         * break; } } if (hasOtherApps) { boolean oldStackStatus = stack.setAutoexec(false); try { FWUrl lastUrl =
         * stack.pop(); while (stack.size() > 2) { stack.pop(); } stack.push(lastUrl); } finally {
         * stack.setAutoexec(oldStackStatus); } } }
         */
    }

    @Override
    public byte getPriority() {
        // TODO Auto-generated method stub
        return 0;
    }

}
