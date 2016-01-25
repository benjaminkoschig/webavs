package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CODecision;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * @author sch
 */
public class COExecuterDecision extends CO002ExecuterDeuxiemeRappel {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(
     * globaz.aquila.db.access.poursuite.COContentieux, globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CODecision decision = new CODecision(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                decision.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                decision.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            if (contentieux.isNew()) {
                decision.setNouveauContentieux(Boolean.TRUE);
                decision.addContentieuxPrevisionnel(contentieux);
            } else {
                decision.addContentieux(contentieux);
            }
            decision.setTaxes(taxes);

            this._envoyerDocument(contentieux, decision);

        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

}
