package globaz.al.helpers.rafam;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.rafam.ALProtocoleRafamProcess;
import globaz.al.vb.rafam.ALAnnonceRafamViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Helper d�di� au viewBean ALAnnonceRafamViewBean
 * 
 * @author SIG
 * 
 */
public class ALAnnonceRafamHelper extends ALAbstractHelper {

    private static final String suspendreAnnonce = "suspendreAnnonce";
    private static final String validerAnnonce = "validerAnnonce";

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("imprimerProtocole".equals(action.getActionPart()) && (viewBean instanceof ALAnnonceRafamViewBean)) {

            try {
                ALProtocoleRafamProcess process = new ALProtocoleRafamProcess();
                process.setSession((BSession) session);
                BProcessLauncher.start(process, false);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;

        }

        if (ALAnnonceRafamHelper.validerAnnonce.equals(action.getActionPart())
                && (viewBean instanceof ALAnnonceRafamViewBean)) {

            try {
                if (((ALAnnonceRafamViewBean) viewBean).getAnnonce().isNew()) {
                    ((ALAnnonceRafamViewBean) viewBean).retrieve();
                }
                ALServiceLocator.getAnnonceRafamCreationService().validerAnnonce(
                        ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel());
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }
        if (ALAnnonceRafamHelper.suspendreAnnonce.equals(action.getActionPart())
                && (viewBean instanceof ALAnnonceRafamViewBean)) {

            try {
                if (((ALAnnonceRafamViewBean) viewBean).getAnnonce().isNew()) {
                    ((ALAnnonceRafamViewBean) viewBean).retrieve();
                }
                ALServiceLocator.getAnnonceRafamCreationService().suspendreAnnonce(
                        ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel());
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }

        if ("creer68c".equals(action.getActionPart()) && (viewBean instanceof ALAnnonceRafamViewBean)) {

            try {
                if (((ALAnnonceRafamViewBean) viewBean).getAnnonce().isNew()) {
                    ((ALAnnonceRafamViewBean) viewBean).retrieve();
                }

                ALServiceLocator.getAnnonceRafamCreationService().create68cForAnnonce(
                        ((ALAnnonceRafamViewBean) viewBean).getAnnonce().getAnnonceRafamModel());
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;
        }

        return super.execute(viewBean, action, session);
    }
}
