/*
 * Cr�� le 20 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.prestation;

import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCotisationJointRepartitionManager extends APCotisationManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APCotisationJointRepartition();
    }
}
