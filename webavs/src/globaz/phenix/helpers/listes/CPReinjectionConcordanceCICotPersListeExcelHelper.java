package globaz.phenix.helpers.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.phenix.db.listes.CPReinjectionConcordanceCICotPersListeExcelViewBean;
import globaz.phenix.process.CPReinjectionConcordanceCotPersCIProcess;

/**
 * @author JPA
 * @since 01 juillet 2010
 */
public class CPReinjectionConcordanceCICotPersListeExcelHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof CPReinjectionConcordanceCICotPersListeExcelViewBean) {

            CPReinjectionConcordanceCotPersCIProcess process = new CPReinjectionConcordanceCotPersCIProcess();
            process.setSession((BSession) viewBean.getISession());
            process.setFileName(((CPReinjectionConcordanceCICotPersListeExcelViewBean) viewBean).getFilename());
            process.setEMailAddress(((CPReinjectionConcordanceCICotPersListeExcelViewBean) viewBean).getEMailAddress());

            try {
                BProcessLauncher.start(process);
            } catch (Exception e) {
                viewBean.setMessage(process.getSession().getLabel("ERREUR_TECHNIQUE")
                        + "\n CPReinjectionConcordanceCICotPersListeExcelHelper" + e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

}
