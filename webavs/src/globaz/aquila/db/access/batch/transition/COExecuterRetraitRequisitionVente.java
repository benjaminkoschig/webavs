package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.print.CO01BRadiationPoursuite;
import globaz.aquila.print.CORetraitRequisitionVente;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author Pascal Lovy, 14-dec-2004
 */
public class COExecuterRetraitRequisitionVente extends COAbstractEnvoyerDocument {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** Liste des destinataires. */
    private boolean destinataireDebiteurChecked = false;
    private String etape_info_5010016;

    protected COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        CORetraitRequisitionVente rrv = null;

        // Génération et envoi du document
        try {
            rrv = new CORetraitRequisitionVente(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                rrv.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                rrv.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            rrv.addContentieux(contentieux);
            rrv.setTypeSaisie(getEtape_info_5010016());
            rrv.setTaxes(taxes);
            rrv.setDestinataire(contentieux.getCompteAnnexe().getTiers());

            if (destinataireDebiteurChecked) {
                rrv.setSendTo(CO01BRadiationPoursuite.SEND_TO_BOTH);
            } else {
                rrv.setSendTo(CO01BRadiationPoursuite.SEND_TO_OP);
            }

            this._envoyerDocument(contentieux, rrv);
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
        if (COEtapeInfoConfig.CS_TYPE_SAISIE_SALAIRE.equals(getEtape_info_5010016())) {
            throw new COTransitionException("AQUILA_RDV_SALAIRE_INTERDIT", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_RDV_SALAIRE_INTERDIT"));
        }
        try {
            COHistorique historiqueRDV = historiqueService.getHistoriqueForLibEtapeTypeSaisie(transaction.getSession(),
                    contentieux, ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE, getEtape_info_5010016());
            if (historiqueRDV == null) {
                throw new COTransitionException("AQUILA_TYPE_SAISIE_NON_TROUVE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_TYPE_SAISIE_NON_TROUVE"));
            }
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_TYPE_SAISIE_NON_TROUVE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_TYPE_SAISIE_NON_TROUVE"));
        }
    }

    /**
     * @return La valeur courante de la propriété
     */
    public boolean getDestinataireDebiteurChecked() {
        return destinataireDebiteurChecked;
    }

    public String getEtape_info_5010016() {
        return etape_info_5010016;
    }

    /**
     * @param destinataireChecked
     *            La nouvelle valeur de la propriété
     */
    public void setDestinataireDebiteurChecked(boolean destinataireChecked) {
        destinataireDebiteurChecked = destinataireChecked;
    }

    public void setEtape_info_5010016(String etape_info_5010016) {
        this.etape_info_5010016 = etape_info_5010016;
    }
}
