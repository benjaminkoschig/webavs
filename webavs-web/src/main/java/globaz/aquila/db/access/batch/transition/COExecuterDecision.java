package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CODecision;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;

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

    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        // Le mode de compensation "Report" ne doit pas bloquer la création du contentieux lors d'un report de délai.
        if (!contentieux.getEtape().getLibEtape().equals(ICOEtape.CS_AUCUNE)
                && APISection.MODE_REPORT.equals(contentieux.getSection().getIdModeCompensation())) {
            throw new COTransitionException("AQUILA_ERR_SECTION_REPORTEE", COActionUtils.getMessage(contentieux
                    .getSession(), "AQUILA_ERR_SECTION_REPORTEE", new String[] { contentieux.getSection()
                    .getIdExterne() }));
        }
    }

}
