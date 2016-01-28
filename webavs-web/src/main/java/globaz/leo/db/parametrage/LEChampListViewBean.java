/*
 * Cr�� le 20 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LEChampListViewBean extends ENChampManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * on ordre par l'id pour avoir les labels dans l'ordre lors de l'affichage � l'�cran !
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
