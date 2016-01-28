package globaz.aquila.db.access.batch.transition;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.util.COActionUtils;
import globaz.globall.db.BTransaction;

/**
 * Effectue les actions spécifiques à la transition.
 * 
 * @author sch
 */
public class COSaisirAvisVenteEnchere extends CODefaultTransitionAction {

    private String etape_info_5010016;

    /**
     * @see globaz.aquila.db.access.batch.transition.COTransitionAction#_validate(globaz.aquila.db.access.poursuite.COContentieux,
     *      globaz.globall.db.BTransaction)
     */
    @Override
    protected void _validate(COContentieux contentieux, BTransaction transaction) throws COTransitionException {
        COHistoriqueService historiqueService = COServiceLocator.getHistoriqueService();
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

    public String getEtape_info_5010016() {
        return etape_info_5010016;
    }

    public void setEtape_info_5010016(String etape_info_5010016) {
        this.etape_info_5010016 = etape_info_5010016;
    }
}
