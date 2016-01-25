package globaz.naos.stack.rules;

import globaz.framework.utils.rules.evals.FWDefaultEval;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (06.09.2002 09:27:36)
 * 
 * @author: Administrator
 */

public class Evaluable extends FWDefaultEval {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWUrlsStack m_stack;

    /**
     * Commentaire relatif au constructeur Evaluable.
     */
    public Evaluable(FWUrlsStack stack, String applicationId) {
        super(stack, applicationId);
        m_stack = stack;
        addValidActions(new String[] { "imprimer" });
    }

    /**
     * Méthode à implémenter pour décrire l'évaluation de cet objet.
     */
    @Override
    protected boolean eval() {

        // printStackStatus();
        return super.eval();
    }

    private void printStackStatus() {

        if ((m_stack != null) && (!JadeStringUtil.isEmpty(m_stack.toString()))) {
            int actionCount = 8;
            if (actionCount > m_stack.size()) {
                actionCount = m_stack.size();
            }
            System.out.println("\n\n*-*-*-*-*-*-*\nLast " + actionCount + " actions:");
            for (int i = 1; i <= actionCount; i++) {
                System.out.println(m_stack.peekAt(m_stack.size() - i).getParam("userAction").getValue());
            }
        }
    }
}
