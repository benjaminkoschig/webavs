/*
 * Cr�� le 5 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
     * G�n�ration et envoi du document
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
                // Est pr�visionnel et n'existe pas encore au contentieux !
                sommation.setNouveauContentieux(Boolean.TRUE);
                sommation.addContentieuxPrevisionnel(contentieux);
            } else {
                sommation.addContentieux(contentieux);
            }

            sommation.setTaxes(taxes);
            sommation.setEBillTransactionID(contentieux.getEBillTransactionID());
            sommation.setEBillPrintable(contentieux.getEBillPrintable());

            this._envoyerDocument(contentieux, sommation);

        } catch (Exception e) {
            // transaction.addErrors(errors)
            throw new COTransitionException(e);
        }
    }

}
