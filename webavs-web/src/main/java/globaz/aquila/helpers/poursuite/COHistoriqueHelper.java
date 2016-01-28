package globaz.aquila.helpers.poursuite;

import globaz.aquila.db.access.batch.COEtapeInfoManager;
import globaz.aquila.db.poursuite.COHistoriqueViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;

/**
 * <H1>Description</H1>
 * <p>
 * Helper pour les action aquila.poursuite.historique.
 * </p>
 * 
 * @author vre
 */
public class COHistoriqueHelper extends FWHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Redéfini pour charger la liste des etapes infos pour cet historique.
     * 
     * @param viewBean
     * @param action
     * @param session
     * @throws Exception
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        // charger les étapes infos pour l'affichage dans l'écran
        COHistoriqueViewBean historique = (COHistoriqueViewBean) viewBean;
        COEtapeInfoManager infoManager = new COEtapeInfoManager();

        infoManager.setForIdHistorique(historique.getIdHistorique());
        infoManager.setISession(session);
        infoManager.setLeftJoin(true);
        infoManager.find();

        ((COHistoriqueViewBean) viewBean).setToutesEtapesInfos(infoManager.getContainer());
    }

}
