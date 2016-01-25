package globaz.hercule.helpers.traitement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.traitement.CERattrapageNumViewBean;
import globaz.hercule.process.traitement.CEMiseAJourNumContEmplProcess;

public class CERattrapageNumHelper extends FWHelper {

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CERattrapageNumViewBean traitementBean = (CERattrapageNumViewBean) viewBean;
        CEMiseAJourNumContEmplProcess process = new CEMiseAJourNumContEmplProcess();
        process.setFromAnne(traitementBean.getFromAnnee());
        process.setUntilAnnee(traitementBean.getUntilAnnee());
        process.setISession(session);
        process.setEMailAddress(traitementBean.getEmail());

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            traitementBean.setMessage(e.toString());
            traitementBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
