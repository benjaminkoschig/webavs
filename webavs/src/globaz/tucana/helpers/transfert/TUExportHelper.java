package globaz.tucana.helpers.transfert;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.process.transfert.TUExportProcess;
import globaz.tucana.vb.transfert.TUExportViewBean;

/**
 * Definition du helper pour le domaine transfert exprotation
 * 
 * @author fgo date de création : 25.08.2006
 * @version : version 1.0
 * 
 */
public class TUExportHelper extends FWHelper {

    /**
     * Constructeur
     */
    public TUExportHelper() {
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
        TUExportViewBean eViewBean = (TUExportViewBean) viewBean;
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        // ACTION GENERER
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        if ("generer".equals(action.getActionPart())) {
            try {
                TUExportProcess process = new TUExportProcess();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setIdBouclement(eViewBean.getIdBouclement());
                process.setAnnee(eViewBean.getAnnee());
                process.setMois(eViewBean.getMois());
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
