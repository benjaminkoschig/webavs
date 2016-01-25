package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO01BRadiationPoursuite;
import globaz.aquila.print.CO10ARetraitPoursuite;
import globaz.aquila.process.COProcessContentieux;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 14-dec-2004
 */
public class CO042ExecuterSuspensionPoursuite extends COAbstractEnvoyerDocument {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** Liste des destinataires. */
    private boolean destinataireDebiteurChecked = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        CO10ARetraitPoursuite rdp = null;

        // Génération et envoi du document
        try {
            rdp = new CO10ARetraitPoursuite(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rdp.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rdp.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rdp.addContentieux(contentieux);
            rdp.setTaxes(taxes);
            rdp.setDestinataire(contentieux.getCompteAnnexe().getTiers());

            if (destinataireDebiteurChecked) {
                rdp.setSendTo(CO01BRadiationPoursuite.SEND_TO_BOTH);
            } else {
                rdp.setSendTo(CO01BRadiationPoursuite.SEND_TO_OP);
            }

            this._envoyerDocument(contentieux, rdp);
        } catch (Exception e) {
            throw new COTransitionException(e);
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getDestinataireDebiteurChecked() {
        return destinataireDebiteurChecked;
    }

    /**
     * @param destinataireChecked
     *            La nouvelle valeur de la propriété
     */
    public void setDestinataireDebiteurChecked(boolean destinataireChecked) {
        destinataireDebiteurChecked = destinataireChecked;
    }
}
