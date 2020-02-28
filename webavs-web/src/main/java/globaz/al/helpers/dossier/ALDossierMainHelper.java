package globaz.al.helpers.dossier;

import ch.globaz.al.business.services.ALServiceLocator;
import globaz.al.helpers.ALAbstractHelper;
import globaz.al.vb.dossier.ALDossierMainViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;

/**
 * Helper d�di� au viewBean ALDossierMainViewBean
 * 
 * @author GMO
 * 
 */
public class ALDossierMainHelper extends ALAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        super._add(viewBean, action, session);

        JadeBusinessMessage[] messages = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN);
        if (messages != null) {
            session.setAttribute("addWarnings", messages);
        }
    }

    /**
     * Initialise le mod�le complex (dossierComplexModel) du viewBean, c'est-�-dire qu'il le charge si il existe ou
     * d�finit les valeurs par d�faut sinon.
     * 
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (viewBean instanceof ALDossierMainViewBean) {

            ((ALDossierMainViewBean) viewBean).loadRequiredCs();

            ((ALDossierMainViewBean) viewBean).setDossierComplexModel(ALServiceLocator.getDossierComplexModelService()
                    .initModel(((ALDossierMainViewBean) viewBean).getDossierComplexModel()));

            ((ALDossierMainViewBean) viewBean).setDossierComplexModel(ALServiceLocator.getDossierComplexModelService()
                    .initModel(((ALDossierMainViewBean) viewBean).getDossierComplexModel()));

            // Charge les droits pour afficher ceux r�cemment cr�es

            if (!JadeNumericUtil.isEmptyOrZero(((ALDossierMainViewBean) viewBean).getDossierComplexModel().getId())) {
                ((ALDossierMainViewBean) viewBean).setDroitsList(ALServiceLocator.getCalculBusinessService().getCalcul(
                        ((ALDossierMainViewBean) viewBean).getDossierComplexModel(),
                        ((ALDossierMainViewBean) viewBean).getDateCalcul()));
            }

        }
        super._init(viewBean, action, session);
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._update(viewBean, action, session);

        JadeBusinessMessage[] messages = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.WARN);

        boolean modificationBeneficiaireDossier = false;
        if (viewBean instanceof ALDossierMainViewBean) {
            modificationBeneficiaireDossier = !(((ALDossierMainViewBean) viewBean).getBeneficiaireAvantModification()
                    .equals(((ALDossierMainViewBean) viewBean).getDossierComplexModel().getDossierModel()
                            .getIdTiersBeneficiaire()));
        }

        if (messages != null && modificationBeneficiaireDossier) {

            session.setAttribute("addWarnings", messages);
        }

    }

    /**
     * Ex�cute les diff�rentes customAction possibles depuis le viewBean
     * 
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("retirerBeneficiaire".equals(action.getActionPart()) && (viewBean instanceof ALDossierMainViewBean)) {
            try {
                if (((ALDossierMainViewBean) viewBean).getDossierComplexModel().isNew()) {
                    ((ALDossierMainViewBean) viewBean).retrieve();
                }
                // Si on retire le tiers b�n�ficaire, on laisse quand m�me le
                // dossier en paiement direct (� l'alloc)
                ((ALDossierMainViewBean) viewBean).setDossierComplexModel(ALServiceLocator.getDossierBusinessService()
                        .retirerBeneficiaire(((ALDossierMainViewBean) viewBean).getDossierComplexModel(), true));

                ((ALDossierMainViewBean) viewBean).update();
                // Il faut refaire un retrieve manuellement, car apr�s cette
                // action, pas de retrieve
                // et les champs spy ne sont plus � jour par rapport au la DB,
                // car modifier via update
                ((ALDossierMainViewBean) viewBean).retrieve();

            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        } else if ("copier".equals(action.getActionPart())) {
            try {

                if (((ALDossierMainViewBean) viewBean).getDossierComplexModel().isNew()) {
                    ((ALDossierMainViewBean) viewBean).retrieve();

                }

                // On affecte un autre dossier au viewBean pour ouvrir le
                // dossier clon� dans l'�cran
                ((ALDossierMainViewBean) viewBean).setDossierComplexModel(ALServiceLocator.getDossierBusinessService()
                        .copierDossier(((ALDossierMainViewBean) viewBean).getDossierComplexModel()));

                // Il faut recharger le viewBean dans le contexte du dossier
                // r�sultat, car sinon les droits / prestations affich�s seront
                // ceux du dossier source
                ((ALDossierMainViewBean) viewBean).retrieve();

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
