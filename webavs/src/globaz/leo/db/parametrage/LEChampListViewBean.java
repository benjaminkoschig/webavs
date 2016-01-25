/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENChampManager;
import globaz.envoi.db.parametreEnvoi.access.IENChampDefTable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEChampListViewBean extends ENChampManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * on ordre par l'id pour avoir les labels dans l'ordre lors de l'affichage à l'écran !
     */
    @Override
    protected String _getOrder(BStatement statement) {

        return IENChampDefTable.ID_CHAMP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEChampViewBean();
    }

}
