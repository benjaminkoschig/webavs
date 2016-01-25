package globaz.draco.stack.rules;

/**
 * Cette classe permet d'évaluer les règles Date de création : (06.09.2002 09:27:36)
 * 
 * @author: Sébastien Chappatte
 */

import globaz.jade.client.util.JadeStringUtil;

public class Evaluable extends globaz.framework.utils.rules.FWEvaluable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.framework.utils.urls.FWUrlsStack m_stack;

    /**
     * Commentaire relatif au constructeur Evaluable.
     */
    public Evaluable(globaz.framework.utils.urls.FWUrlsStack stack) {
        super();
        m_stack = stack;
    }

    /**
     * Méthode à implémenter pour décrire l'évaluation de cet objet.
     */
    @Override
    protected boolean eval() {
        String action = null;
        if ((m_stack != null) && (!JadeStringUtil.isBlank(m_stack.toString()))) {
            try {
                action = m_stack.peek().getParam("userAction").toString();
            } catch (Exception e) {
                action = "";
            }
            if ((!JadeStringUtil.isBlank(action)) && (action.indexOf(".chercher") == -1)
                    && (action.indexOf(".afficher") == -1)) {
                return true;
            }
        }
        return false;
    }
}