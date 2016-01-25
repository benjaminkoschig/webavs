package globaz.aquila.db.access.batch.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.CO01BRadiationPoursuite;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 14-dec-2004
 */
public class CO038ExecuterRadiationPoursuite extends COAbstractEnvoyerDocument {

    /** Liste des destinataires */
    private boolean destinataireDebiteurChecked = false;

    /** Motif de la radiation */
    private String motifRadiation = "";

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        CO01BRadiationPoursuite rdp = null;

        // Génération et envoi du document
        try {
            rdp = new CO01BRadiationPoursuite(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rdp.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rdp.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rdp.addContentieux(contentieux);
            rdp.setTaxes(taxes);
            IntTiers affilie = contentieux.getCompteAnnexe().getTiers();
            rdp.setDestinataire(affilie);
            rdp.setMotifRadiation(motifRadiation);

            if (destinataireDebiteurChecked) {
                rdp.setSendTo(CO01BRadiationPoursuite.SEND_TO_BOTH);
            } else {
                rdp.setSendTo(CO01BRadiationPoursuite.SEND_TO_OP);
            }

            this._envoyerDocument(contentieux, rdp, false);
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
        if (JadeStringUtil.isEmpty(motifRadiation)) {
            throw new COTransitionException("AQUILA_ERR_CO038_MOTIF_RADIATION_VIDE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_ERR_CO038_MOTIF_RADIATION_VIDE"));
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getDestinataireDebiteurChecked() {
        return destinataireDebiteurChecked;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getMotifRadiation() {
        return motifRadiation;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setDestinataireDebiteurChecked(boolean destinataireChecked) {
        destinataireDebiteurChecked = destinataireChecked;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setMotifRadiation(String motif) {
        motifRadiation = motif;
    }

}
