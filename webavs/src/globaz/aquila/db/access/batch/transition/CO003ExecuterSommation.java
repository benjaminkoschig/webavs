/*
 * Créé le 5 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO00CSommationPaiement;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * @author dostes
 */
public class CO003ExecuterSommation extends COAbstractDelaiPaiementAction {
    /**
     * Génération et envoi du document
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        try {
            CO00CSommationPaiement sommation = new CO00CSommationPaiement(transaction.getSession());

            sommation.setDateDelaiPaiement(dateDelaiPaiement);
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                sommation.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                sommation.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }

            if (contentieux.isNew()) {
                // Est prévisionnel et n'existe pas encore au contentieux !
                sommation.setNouveauContentieux(Boolean.TRUE);
                sommation.addContentieuxPrevisionnel(contentieux);
            } else {
                sommation.addContentieux(contentieux);
            }

            sommation.setTaxes(taxes);
            this._envoyerDocument(contentieux, sommation);

        } catch (Exception e) {
            // transaction.addErrors(errors)
            throw new COTransitionException(e);
        }
    }

}
