package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.print.CO15RappelOpRdv;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author kurkus, 30 nov. 04
 */
public class CO017ExecuterRappelRDV extends COAbstractEnvoyerDocument {

    private String etape_info_5010016 = "";
    protected COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();

    private String typeSaisie = null;

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_execute(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _execute(COContentieux contentieux, List taxes, BTransaction transaction)
            throws COTransitionException {
        // Génération et envoi du document
        try {
            CO15RappelOpRdv ror = new CO15RappelOpRdv(transaction.getSession());
            if ((getParent() == null)
                    || JadeStringUtil.isBlank(((COProcessContentieux) getParent()).getUserIdCollaborateur())) {
                ror.setCollaborateur(transaction.getSession().getUserInfo());
            } else {
                ror.setCollaborateur(((COProcessContentieux) getParent()).getUser());
            }
            ror.addContentieux(contentieux);
            ror.setTaxes(taxes);
            ror.setTypeSaisie(typeSaisie);
            this._envoyerDocument(contentieux, ror);
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
        super._validate(contentieux, transaction);

        // Test des préconditions
        _validerSolde(contentieux);
        _validerEcheance(contentieux);

        if (!JadeStringUtil.isBlank(getEtape_info_5010016())) {
            typeSaisie = getEtape_info_5010016();
        } else {
            for (Iterator infoIter = etapeInfosParLibelle.entrySet().iterator(); infoIter.hasNext();) {
                Map.Entry entry = (Map.Entry) infoIter.next();

                if (COEtapeInfoConfig.CS_TYPE_SAISIE.equals(entry.getKey())) {
                    typeSaisie = (String) entry.getValue();
                    break;
                }
            }
        }

        if (COEtapeInfoConfig.CS_TYPE_SAISIE_SALAIRE.equals(typeSaisie)) {
            throw new COTransitionException("AQUILA_RDV_SALAIRE_INTERDIT", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_RDV_SALAIRE_INTERDIT"));
        }
        try {
            COHistorique historiqueRDV = historiqueService.getHistoriqueForLibEtapeTypeSaisie(transaction.getSession(),
                    contentieux, ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE, typeSaisie);
            if (historiqueRDV == null) {
                throw new COTransitionException("AQUILA_TYPE_SAISIE_NON_TROUVE", COActionUtils.getMessage(
                        contentieux.getSession(), "AQUILA_TYPE_SAISIE_NON_TROUVE"));
            }
        } catch (Exception e) {
            throw new COTransitionException("AQUILA_TYPE_SAISIE_NON_TROUVE", COActionUtils.getMessage(
                    contentieux.getSession(), "AQUILA_TYPE_SAISIE_NON_TROUVE"));
        }

    }

    public String getEtape_info_5010016() {
        return etape_info_5010016;
    }

    public void setEtape_info_5010016(String etape_info_5010016) {
        this.etape_info_5010016 = etape_info_5010016;
    }

}
