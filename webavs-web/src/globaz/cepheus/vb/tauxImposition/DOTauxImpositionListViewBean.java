/*
 * Créé le 26 sept. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.cepheus.vb.tauxImposition;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DOTauxImpositionListViewBean extends PRTauxImpositionManager implements FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new DOTauxImpositionViewBean();
    }

}
