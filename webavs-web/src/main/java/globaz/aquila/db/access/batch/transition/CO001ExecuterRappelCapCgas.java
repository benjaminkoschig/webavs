/*
 * Créé le 5 sept. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO00ARappelPaiementCapCgas;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

import java.util.List;

/**
 * Cette classe est appelée pour créer un document de Rappel pour les instances CCVD et Agrivit pour la séquence CAP/CGAS.
 *
 * @author ebsc
 */
public class CO001ExecuterRappelCapCgas extends COAbstractDelaiPaiementAction {

    /*
     * (non-Javadoc)
     *
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(
     * globaz.aquila.db.access.poursuite.COContentieux, globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        try {
            CO00ARappelPaiementCapCgas rappel = new CO00ARappelPaiementCapCgas(transaction.getSession());
                rappel.setDateDelaiPaiement(dateDelaiPaiement);

            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rappel.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rappel.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            if (contentieux.isNew()) {
                rappel.setNouveauContentieux(Boolean.TRUE);
                rappel.addContentieuxPrevisionnel(contentieux);
            } else {
                rappel.addContentieux(contentieux);
            }
            rappel.setTaxes(taxes);
            this._envoyerDocument(contentieux, rappel);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

}
