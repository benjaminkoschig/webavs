/*
 * Créé le 17 janv. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.compte;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * @author jmc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIEcrituresSuspensListViewBean extends CIEcritureManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CIEcrituresSuspensListViewBean() {
        super();
        // on active le control pour la sécurité CI par rapport à une
        // affiliation
        setCacherEcritureEnSuspensProtege(1);// 1=true, 0=false
    }

}
