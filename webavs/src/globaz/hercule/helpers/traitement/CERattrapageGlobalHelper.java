package globaz.hercule.helpers.traitement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.traitement.CETraitementViewBean;
import globaz.hercule.process.traitement.CERattrapageGlobalProcess;

/**
 * @author SCO
 * @since 28 juin 2010
 */
public class CERattrapageGlobalHelper extends FWHelper {

    /**
     * Constructeur de CERattrapageGlobalHelper
     */
    public CERattrapageGlobalHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CETraitementViewBean traitementBean = (CETraitementViewBean) viewBean;
        CERattrapageGlobalProcess process = new CERattrapageGlobalProcess();

        process.setISession(session);
        process.setAnnee(traitementBean.getAnnee());
        process.setEMailAddress(traitementBean.getEmail());

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            traitementBean.setMessage(e.toString());
            traitementBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

}
