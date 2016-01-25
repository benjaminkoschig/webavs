package globaz.cygnus.helpers.qds;

import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFSaisieQdViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author jje
 */
public class RFSaisieQdChoixGenreHelper extends PRAbstractHelper {

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validerAfficherChoixGenreQd((RFSaisieQdViewBean) viewBean);
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Valide l'affichage du 2eme écran de saisie
     * 
     * @param RFSaisieQdViewBean
     * @throws Exception
     */
    private void validerAfficherChoixGenreQd(RFSaisieQdViewBean viewBean) throws Exception {

        if (JadeStringUtil.isBlank(viewBean.getIdTiers())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TIERS_NON_SELECTIONNE");
        }

        if (JadeStringUtil.isBlank(viewBean.getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_GESTIONNAIRE_NON_SELECTIONNE");
        }

        // Contrôle si un dossier exsiste pour cet assuré
        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            RFPrDemandeJointDossier rfPrDemandeJointDossier = RFUtils.getDossierJointPrDemande(viewBean.getIdTiers(),
                    viewBean.getSession());
            if (null == rfPrDemandeJointDossier) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_DOSSIER_NON_CREE");
            } else {
                viewBean.setIdDossier(rfPrDemandeJointDossier.getIdDossier());
            }
        }
    }
}
