package globaz.naos.helpers.taxeCo2;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.taxeCo2.AFReinjectionListeExcelViewBean;
import globaz.naos.process.taxeCo2.AFReinjectionTaxeCo2Process;

/**
 * @author JPA
 * @since 01 juillet 2010
 */
public class AFReinjectionListeExcelHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof AFReinjectionListeExcelViewBean) {

            AFReinjectionTaxeCo2Process process = new AFReinjectionTaxeCo2Process();
            process.setSession((BSession) viewBean.getISession());
            process.setFileName(((AFReinjectionListeExcelViewBean) viewBean).getFilename());
            process.setEMailAddress(((AFReinjectionListeExcelViewBean) viewBean).getEMailAddress());

            try {
                BProcessLauncher.start(process);
            } catch (Exception e) {
                viewBean.setMessage(process.getSession().getLabel("ERREUR_TECHNIQUE") + "\n AFReinjectionTaxeCo2Helper"
                        + e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

}
