package globaz.tucana.helpers.transfert;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.process.transfert.TUImportationProcess;
import globaz.tucana.vb.transfert.TUImportationViewBean;

/**
 * Definition du helper pour le domaine transfert exprotation
 * 
 * @author fgo date de création : 25.08.2006
 * @version : version 1.0
 * 
 */
public class TUImportationHelper extends FWHelper {

    /**
     * Constructeur
     */
    public TUImportationHelper() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#execute(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        TUImportationViewBean eViewBean = (TUImportationViewBean) viewBean;
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        // ACTION GENERER
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        if ("importer".equals(action.getActionPart())) {
            try {
                TUImportationProcess process = new TUImportationProcess();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMailAdress());
                process.setSource(JadeStringUtil.change(eViewBean.getFileName(), "\\", "/"));
                BProcessLauncher.start(process);
                // process.executeProcess();
                if (process.isOnError()) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(process.getMessage());
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            return viewBean;
        } else {
            return super.execute(viewBean, action, session);
        }
    }
}
