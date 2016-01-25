package globaz.campus.stack.rules;

/**
 * Insérez la description du type ici. Date de création : (06.09.2002 09:27:36)
 * 
 * @author: Administrator
 */
import globaz.campus.application.GEApplication;
import globaz.framework.controller.FWAction;
import globaz.framework.utils.rules.evals.FWDefaultEval;

public class Evaluable extends FWDefaultEval {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.framework.utils.urls.FWUrlsStack mstack = null;

    /**
     * Commentaire relatif au constructeur Evaluable.
     */
    public Evaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, GEApplication.DEFAULT_APPLICATION_CAMPUS);
        mstack = stack;
    }

    @Override
    public boolean evalApplicationAction(FWAction action) {

        // if (action.getClassPart().equals("moyenCommunication")) {
        // if (action.getActionPart().indexOf("chercher") == -1) {
        // return true;
        // }
        // }
        // if("campus.lots.lots.chercher".equals(action.toString())){
        // while(mstack.size()>1){
        // mstack.pop();
        // }
        // mstack.push(action);
        // }
        return false;
    }
}
