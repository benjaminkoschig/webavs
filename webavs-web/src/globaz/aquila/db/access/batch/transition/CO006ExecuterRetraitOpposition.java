package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO03ARetraitOpposition;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 17-nov-2004
 */
public class CO006ExecuterRetraitOpposition extends COAbstractEnvoyerDocument {

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CO03ARetraitOpposition ro = new CO03ARetraitOpposition(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                ro.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                ro.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            ro.setTaxes(taxes);
            ro.addContentieux(contentieux);

            this._envoyerDocument(contentieux, ro);
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
        // @TODO Contrôler que l'affilié ait effectivement fait opposition
    }

}
