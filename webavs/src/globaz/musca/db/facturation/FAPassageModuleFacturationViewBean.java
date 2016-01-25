/*
 * Créé le 23 févr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.musca.process.FAPassageModuleFacturationProcess;

/**
 * @author rri Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
