/*
 * Cr�� le 23 f�vr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.musca.process.FAPassageModuleFacturationProcess;

/**
 * @author rri Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class FAPassageModuleFacturationViewBean extends FAPassageModuleFacturationProcess implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FAPassageModuleFacturationViewBean() {
        super();
        setIdModuleFact(null);
    }

    public FAPassageModuleFacturationViewBean(String s) {
        super();
        setIdModuleFact(s);
    }

}
