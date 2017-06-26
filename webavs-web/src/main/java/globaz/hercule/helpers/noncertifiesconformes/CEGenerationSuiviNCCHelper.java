/*
 * Globaz SA.
 */
package globaz.hercule.helpers.noncertifiesconformes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.hercule.db.noncertifiesconformes.CEGenerationSuiviNCCViewBean;
import globaz.hercule.process.noncertifiesconformes.CEGenerationSuiviNCCProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CEGenerationSuiviNCCHelper extends FWHelper {

    private static final Logger LOG = LoggerFactory.getLogger(CEGenerationSuiviNCCHelper.class);

    public CEGenerationSuiviNCCHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CEGenerationSuiviNCCViewBean geneSuiviBean = (CEGenerationSuiviNCCViewBean) viewBean;
        CEGenerationSuiviNCCProcess process = new CEGenerationSuiviNCCProcess();

        process.setISession(session);
        process.setAnnee(geneSuiviBean.getAnnee());
        process.setEMailAddress(geneSuiviBean.getEmail());
        process.setForNumAffilie(geneSuiviBean.getForNumAffilie());

        try {
            BProcessLauncher.start(process);

        } catch (Exception e) {
            LOG.error(this.getClass().getName(), "Error when the process CEGenerationSuiviNCCProcess start");
            geneSuiviBean.setMessage(e.toString());
            geneSuiviBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
