/*
 * Cr�� le 17 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.musca.db.facturation;

import globaz.globall.db.BEntity;

/**
 * @author mmu Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */

public class FAAfactAQuittancerListViewBean extends FAAfactAQuittancerManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FAAfactAQuittancerViewBean();
    }
}
