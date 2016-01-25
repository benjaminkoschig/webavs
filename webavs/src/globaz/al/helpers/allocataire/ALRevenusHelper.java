package globaz.al.helpers.allocataire;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.allocataire.ALRevenusViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeDateUtil;
import java.util.Date;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper dédié au viewBean ALRevenusViewBean
 * 
 * @author GMO
 */
public class ALRevenusHelper extends ALAbstractHelper {

    /**
     * Initialise le modèle (revenuModel) du viewBean, c'est-à-dire qu'il le charge si il existe ou définit les valeurs
     * par défaut sinon.
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        ((ALRevenusViewBean) viewBean).setRevenuModel(ALServiceLocator.getRevenuModelService().initModel(
                ((ALRevenusViewBean) viewBean).getRevenuModel()));
        // Chargement de l'allocataire
        ((ALRevenusViewBean) viewBean).setAllocataireComplexModel(ALServiceLocator.getAllocataireComplexModelService()
                .read(((ALRevenusViewBean) viewBean).getRevenuModel().getIdAllocataire()));

        // On charge la recherche ici car pour cette écran on est
        // toujours en mode nouveau revenu
        // car on peut déjà en saisir (et retrieve du viewBean n'est
        // jamais appelé)

        ((ALRevenusViewBean) viewBean).setRevenuSearchModel(ALServiceLocator.getRevenuModelService().search(
                ((ALRevenusViewBean) viewBean).getRevenuSearchModel()));

        Date today = new Date();
        // Chargement du revenu le plus récent pour l'allocataire
        RevenuModel revenuModel = ALServiceLocator.getRevenuModelService().searchDernierRevenu(
                JadeDateUtil.getGlobazFormattedDate(today),
                ((ALRevenusViewBean) viewBean).getRevenuModel().getIdAllocataire(), false);

        if (revenuModel == null) {
            revenuModel = new RevenuModel();
            revenuModel.setMontant("");
        }

        ((ALRevenusViewBean) viewBean).setDernierRevenuAlloc(revenuModel);

        // Chargement du revenu le plus récent pour le conjoint de l'allocataire
        revenuModel = ALServiceLocator.getRevenuModelService().searchDernierRevenu(
                JadeDateUtil.getGlobazFormattedDate(today),
                ((ALRevenusViewBean) viewBean).getRevenuModel().getIdAllocataire(), true);

        if (revenuModel == null) {
            revenuModel = new RevenuModel();
            revenuModel.setMontant("");
        }

        ((ALRevenusViewBean) viewBean).setDernierRevenuConj(revenuModel);

        super._init(viewBean, action, session);
    }

    /**
     * Exécute les différentes customAction possibles depuis le viewBean
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("supprimerRevenu".equals(action.getActionPart()) && (viewBean instanceof ALRevenusViewBean)) {
            try {
                if (((ALRevenusViewBean) viewBean).getRevenuModel().isNew()) {
                    ((ALRevenusViewBean) viewBean).retrieve();
                }
                ((ALRevenusViewBean) viewBean).delete();

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }

}
