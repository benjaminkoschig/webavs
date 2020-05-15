/*
 * Cr�� le 31 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.globall.db.BEntity;

/**
 *
 *
 */
public class APEnfantPanManager extends APSituationFamilialePanManager {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cr�e une nouvelle instance de la classe APEnfantMatManager.
     */
    public APEnfantPanManager() {
        super(IAPDroitMaternite.CS_TYPE_ENFANT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APEnfantPan();
    }
}
