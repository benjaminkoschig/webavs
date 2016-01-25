package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO13RappelOpPaiement;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author kurkus, 30 nov. 04
 */
public class CO015ExecuterRappelSaisie extends COAbstractEnvoyerDocument {

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CO13RappelOpPaiement rop = new CO13RappelOpPaiement(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rop.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rop.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rop.setTaxes(taxes);
            rop.addContentieux(contentieux);
            this._envoyerDocument(contentieux, rop);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        super._validate(contentieux, transaction);
        // Test des préconditions
        _validerSolde(contentieux);
        _validerEcheance(contentieux);
    }

}
