package globaz.al.helpers.droit;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.droit.ALDroitViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper dédié au viewBean ALDroitViewBean
 * 
 * @author GMO
 * 
 */
public class ALDroitHelper extends ALAbstractHelper {

    /**
     * Initialise le modèle (droitComplexModel) du viewBean, c'est-à-dire qu'il le charge si il existe ou définit les
     * valeurs par défaut sinon.
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            if (viewBean instanceof ALDroitViewBean) {

                // On initialise le droit avec les valeurs par défaut
                DroitComplexModel droitComplexModel = ALServiceLocator.getDroitComplexModelService().initModel(
                        ((ALDroitViewBean) viewBean).getDroitComplexModel());
                ((ALDroitViewBean) viewBean).setDroitComplexModel(droitComplexModel);
                // On charge le dossier lié au nouveau droit
                ((ALDroitViewBean) viewBean).setDossierComplexModel(ALServiceLocator.getDossierComplexModelService()
                        .read(((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel().getIdDossier()));

            }
        } catch (JadeApplicationException e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } catch (JadePersistenceException e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        super._init(viewBean, action, session);
    }

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._add(viewBean, action, session);
        JadeBusinessMessage[] messages = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN);
        if (messages != null) {

            session.setAttribute("addWarnings", messages);
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._update(viewBean, action, session);

        JadeBusinessMessage[] messages = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN);

        boolean modificationBeneficiaireDroit = false;
        if (viewBean instanceof ALDroitViewBean) {
            modificationBeneficiaireDroit = !(((ALDroitViewBean) viewBean).getBeneficiaireAvantModification()
                    .equals(((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel()
                            .getIdTiersBeneficiaire()));
        }

        if (messages != null && modificationBeneficiaireDroit) {

            session.setAttribute("addWarnings", messages);
        }
    }

    /**
     * Exécute les différentes customAction possibles depuis le viewBean
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("toformation".equals(action.getActionPart()) && (viewBean instanceof ALDroitViewBean)) {

            if (JadeStringUtil.isBlank(((ALDroitViewBean) viewBean).getId())) {
                viewBean.setMessage("Impossible de convertir un nouveau droit");
                viewBean.setMsgType(FWViewBeanInterface.WARNING);
            } else {

                try {
                    if (((ALDroitViewBean) viewBean).getDroitComplexModel().isNew()) {
                        ((ALDroitViewBean) viewBean).retrieve();
                    }
                    ((ALDroitViewBean) viewBean).setDroitComplexModel(ALServiceLocator.getDroitBusinessService()
                            .copieToFormation(((ALDroitViewBean) viewBean).getDroitComplexModel()));

                    ((ALDroitViewBean) viewBean).setEcheanceCalculee("");

                } catch (Exception e) {
                    viewBean.setMessage(e.toString());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            }
            return viewBean;
        }

        if ("supprimerDroit".equals(action.getActionPart()) && (viewBean instanceof ALDroitViewBean)) {
            try {
                if (((ALDroitViewBean) viewBean).getDroitComplexModel().isNew()) {
                    ((ALDroitViewBean) viewBean).retrieve();
                }
                ((ALDroitViewBean) viewBean).delete();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }
        if ("retirerBeneficiaire".equals(action.getActionPart()) && (viewBean instanceof ALDroitViewBean)) {
            try {
                if (((ALDroitViewBean) viewBean).getDroitComplexModel().isNew()) {
                    ((ALDroitViewBean) viewBean).retrieve();
                }
                ((ALDroitViewBean) viewBean).getDroitComplexModel().getDroitModel().setIdTiersBeneficiaire("0");
                ((ALDroitViewBean) viewBean).update();

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }

        else {
            return super.execute(viewBean, action, session);
        }
    }

}
