package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO11RappelOpPvSaisie;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author kurkus, 30 nov. 04
 */
public class CO014ExecuterRappelPVSaisie extends COAbstractEnvoyerDocument {

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CO11RappelOpPvSaisie rops = new CO11RappelOpPvSaisie(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rops.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rops.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rops.addContentieux(contentieux);
            rops.setTaxes(taxes);
            this._envoyerDocument(contentieux, rops);
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
