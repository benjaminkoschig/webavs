package globaz.hercule.helpers.traitement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.process.traitement.CETraitementReinjectionProcess;
import globaz.hercule.vb.traitement.CEReinjectionViewBean;

/**
 * @author JPA
 * @since 01 juillet 2010
 */
public class CEReinjectionHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if (viewBean instanceof CEReinjectionViewBean) {

            CETraitementReinjectionProcess process = new CETraitementReinjectionProcess();
            process.setSession((BSession) viewBean.getISession());
            process.setFileName(((CEReinjectionViewBean) viewBean).getFilename());
            process.setEMailAddress(((CEReinjectionViewBean) viewBean).getEMailAddress());

            try {
                BProcessLauncher.start(process);
            } catch (Exception e) {
                viewBean.setMessage(process.getSession().getLabel("ERREUR_TECHNIQUE") + "\n CEReinjectionHelper"
                        + e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            super._start(viewBean, action, session);
        }
    }

}
