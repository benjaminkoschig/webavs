package globaz.musca.stack.rules;

import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.musca.application.FAApplication;

public class Evaluable extends FWDefaultEval {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur Evaluable.
     */
    public Evaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super(stack, FAApplication.DEFAULT_APPLICATION_MUSCA);
        addValidActions(new String[] { "comptabiliser", "imprimer", "listes", "listerAfacts", "listerCompensations",
                "listerDecomptes", "genererIntMoratoire", "genererSoldeBVR", "aQuittancer" });
    }
    /**
     * Méthode à implémenter pour décrire l'évaluation de cet objet.
     */
    /*
     * protected boolean eval() { String action = null; if ((m_stack
     * !=null)&&(!JadeStringUtil.isBlank(m_stack.toString()))) { try { action =
     * m_stack.peek().getParam("userAction").toString(); } catch (Exception e) { action = ""; JadeLogger.error(this,e);
     * } /*if ((action.indexOf(".lister") !=-1) || (action.indexOf("=back") != -1)){ return true; }
     */
    /*
     * if ((!JadeStringUtil.isBlank(action))&&(action.indexOf(".chercher")==-1) && (action.indexOf(".afficher")==-1)){
     * return true; } } return false; }
     */
}
