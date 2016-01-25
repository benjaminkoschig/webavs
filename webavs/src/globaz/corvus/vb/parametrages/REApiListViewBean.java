/*
 * Créé le 7 sept. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */

package globaz.corvus.vb.parametrages;

import globaz.corvus.db.parametrages.ReParametrageAPIManager;
import globaz.globall.db.BEntity;

public class REApiListViewBean extends ReParametrageAPIManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {

        return new REApiViewBean();
    }

}
