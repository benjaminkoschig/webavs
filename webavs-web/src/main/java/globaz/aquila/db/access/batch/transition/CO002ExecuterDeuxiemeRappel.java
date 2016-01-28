/*
 * Cr�� le 5 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO00BRappelPaiement;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * @author dostes Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CO002ExecuterDeuxiemeRappel extends COAbstractEnvoyerDocument {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(
     * globaz.aquila.db.access.poursuite.COContentieux, globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // G�n�ration et envoi du document
        try {
            CO00BRappelPaiement rappel = new CO00BRappelPaiement(transaction.getSession());
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
