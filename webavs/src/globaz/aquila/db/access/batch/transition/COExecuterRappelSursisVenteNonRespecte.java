package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CORappelSursisVenteNonRespecte;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author sch, 17.10.2007
 */
public class COExecuterRappelSursisVenteNonRespecte extends COAbstractEnvoyerDocument {

    protected COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CORappelSursisVenteNonRespecte ror = new CORappelSursisVenteNonRespecte(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                ror.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                ror.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            ror.addContentieux(contentieux);
            ror.setTaxes(taxes);
            this._envoyerDocument(contentieux, ror);
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
