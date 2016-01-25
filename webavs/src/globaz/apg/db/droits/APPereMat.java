/*
 * Créé le 31 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APPereMat extends APSituationFamilialeMat {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ERREUR_NO_AVS_REQUIS = "NUMERO_AVS_REQUIS";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPereMat.
     */
    protected APPereMat() {
        super(IAPDroitMaternite.CS_TYPE_PERE);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // le père est facultatif dans la saisie du droit

        if (!JadeStringUtil.isBlankOrZero(noAVS)) {

            try {

                if (NSUtil.unFormatAVS(noAVS).length() > 11) {
                    if (!NSUtil.nssCheckDigit(noAVS)) {
                        throw new JAException(getSession().getLabel("NUMERO_AVS_REQUIS"));
                    }
                } else {
                    JAUtil.checkAvs(noAVS);
                }

            } catch (JAException e) {
                // le no AVS est invalide, ne pas insérer dans les tiers
                getSession().addError(getSession().getLabel("NUMERO_AVS_REQUIS"));
                return;
            }
        }
    }
}
