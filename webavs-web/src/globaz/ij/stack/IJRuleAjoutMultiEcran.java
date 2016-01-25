/*
 * Cr�� le 7 juin 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.ij.stack;

import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;

/**
 * @author hpe
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class IJRuleAjoutMultiEcran implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.utils.urls.rules.FWUrlsStackRule#applyOn(globaz.framework .utils.urls.FWUrlsStack)
     */
    @Override
    public void applyOn(FWUrlsStack aStack) {
        if (aStack.size() < 1) {
            return;
        }
        // si la dreni�re URL > useraCTION = ij.prononces.requerant.afficher
        FWUrl lastUrl = aStack.peek();
        FWParamString lastUserAction = lastUrl.getParam("userAction");
        FWParamString method = lastUrl.getParam("_method");
        if (method == null) {
            return;
        }
        if ("ij.prononces.requerant.afficher".equals(lastUserAction.getValue())) {
            if ("add".equals(method.getValue())) {
                // aStack.pop();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.utils.urls.rules.FWUrlsStackRule#getPriority()
     */
    @Override
    public byte getPriority() {
        // TODO Raccord de m�thode auto-g�n�r�
        return 100;
    }

}
