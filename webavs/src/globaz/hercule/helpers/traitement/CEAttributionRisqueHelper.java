package globaz.hercule.helpers.traitement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.hercule.db.traitement.CEAttributionRisqueViewBean;
import globaz.hercule.process.traitement.CEAttributionRisquelProcess;

/**
 * @author SCO
 * @since SCO 7 juin 2010
 */
public class CEAttributionRisqueHelper extends FWHelper {

    /**
     * Constructeur de CERattrapageAnnuelHelper
     */
    public CEAttributionRisqueHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEAttributionRisqueViewBean traitementBean = (CEAttributionRisqueViewBean) viewBean;
        CEAttributionRisquelProcess process = new CEAttributionRisquelProcess();

        process.setSession((BSession) session);
        process.setCodeNoga(traitementBean.getCodeNoga());
        process.setPeriodicite(traitementBean.getPeriodicite());
        process.setEMailAddress(traitementBean.getEmail());

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            traitementBean.setMessage(e.toString());
            traitementBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
