package globaz.phenix.stack.rules;

/**
 * Insérez la description du type ici. Date de création : (06.09.2002 09:27:36)
 * 
 * @author: Administrator
 */
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

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
        if ((m_stack != null) && (!JadeStringUtil.isEmpty(m_stack.toString()))) {
            try {
                action = m_stack.peek().getParam("userAction").toString();
            } catch (Exception e) {
                JadeLogger.error(this, e);
                e.printStackTrace();

                action = "";
            }
            /*
             * if ((action.indexOf(".lister") !=-1) || (action.indexOf("=back") != -1)){ return true; }
             */

            if ((!JadeStringUtil.isEmpty(action)) && (action.indexOf(".chercher") == -1)
                    && (action.indexOf(".afficher") == -1)) {
                return true;
            }
        }

        return false;

    }
}
