/*
 * Créé le 16 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPParametrePlausibiliteViewBean extends CPParametrePlausibilite implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String typeSelected(int i) {
        if (getType().equals("")) {
            setType("0");
        }
        if (Integer.parseInt(getType()) == i) {
            return "selected=\"selected\"";
        } else {
            return "";
        }
    }

}
