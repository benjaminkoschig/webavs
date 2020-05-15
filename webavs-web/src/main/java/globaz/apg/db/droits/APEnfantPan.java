/*
 * Cr�� le 31 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.globall.db.BStatement;

/**
 *
 *
 */
public class APEnfantPan extends APSituationFamilialePan {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String ERREUR_DATE_NAISSANCE_INCORRECTE = "DATE_NAISSANCE_INCORRECTE";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe APEnfantPan.
     */
    protected APEnfantPan() {
        super(IAPDroitMaternite.CS_TYPE_ENFANT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (_propertyMandatory(statement.getTransaction(), dateNaissance,
                getSession().getLabel(ERREUR_DATE_NAISSANCE_INCORRECTE))) {
            _checkDate(statement.getTransaction(), dateNaissance,
                    getSession().getLabel(ERREUR_DATE_NAISSANCE_INCORRECTE));
        }
    }
}
