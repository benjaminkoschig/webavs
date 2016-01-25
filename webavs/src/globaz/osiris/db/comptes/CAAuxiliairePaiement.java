/*
 * Créé le Apr 19, 2005
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.comptes;

import globaz.osiris.api.APIAuxiliairePaiement;
import globaz.osiris.api.APIOperation;

/**
 * @author dda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAAuxiliairePaiement extends CAAuxiliaire implements APIAuxiliairePaiement {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAAuxiliairePaiement() {
        super();
        setIdTypeOperation(APIOperation.CAAUXILIAIRE_PAIEMENT);
    }

    public CAAuxiliairePaiement(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAAUXILIAIRE_PAIEMENT);
    }

}
