package globaz.al.helpers.rafam;

import globaz.al.helpers.ALAbstractHelper;
import globaz.al.process.rafam.ALProtocoleRafamProcess;
import globaz.al.vb.rafam.ALAnnonceRafamEDViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;

public class ALAnnonceRafamEDHelper extends ALAbstractHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if ("imprimerProtocole".equals(action.getActionPart()) && (viewBean instanceof ALAnnonceRafamEDViewBean)) {

            try {
                ALProtocoleRafamProcess process = new ALProtocoleRafamProcess();
                process.setIdEmployeurDelegue(((ALAnnonceRafamEDViewBean) viewBean).getIdEmployeur());
                process.setSession((BSession) session);
                BProcessLauncher.start(process, false);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
            return viewBean;

        }

        return super.execute(viewBean, action, session);
    }

}
