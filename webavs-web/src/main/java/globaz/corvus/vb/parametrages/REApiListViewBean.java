/*
 * Cr�� le 7 sept. 07
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
