package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.COSommationBN;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

public class COExecuterSommationBN extends COAbstractDelaiPaiementAction {

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
            COSommationBN sommation = new COSommationBN(transaction.getSession());

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
