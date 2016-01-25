package globaz.hercule.helpers.declarationStructuree;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.declarationStructuree.CEGenerationSuiviViewBean;
import globaz.hercule.process.declarationStructuree.CEGenerationSuiviProcess;

/**
 * @author SCO
 * @since 2 août 2010
 */
public class CEGenerationSuiviHelper extends FWHelper {

    /**
     * Constructeur de CEGenerationSuiviHelper
     */
    public CEGenerationSuiviHelper() {
        super();
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEGenerationSuiviViewBean geneSuiviBean = (CEGenerationSuiviViewBean) viewBean;
        CEGenerationSuiviProcess process = new CEGenerationSuiviProcess();

        process.setISession(session);
        process.setAnnee(geneSuiviBean.getAnnee());
        process.setEMailAddress(geneSuiviBean.getEmail());
        process.setFromNumAffilie(geneSuiviBean.getFromNumAffilie());
        process.setToNumAffilie(geneSuiviBean.getToNumAffilie());

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            geneSuiviBean.setMessage(e.toString());
            geneSuiviBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
