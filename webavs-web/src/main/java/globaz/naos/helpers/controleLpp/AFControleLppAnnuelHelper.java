/*
 * Globaz SA
 */
package globaz.naos.helpers.controleLpp;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.naos.db.controleLpp.AFControleLppAnnuelViewBean;
import globaz.naos.process.AFControleLppAnnuelProcess;

/**
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFControleLppAnnuelHelper extends FWHelper {

    /**
     * Constructeur de AFControleLppAnnuelHelper
     */
    public AFControleLppAnnuelHelper() {
        super();
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        AFControleLppAnnuelViewBean vb = (AFControleLppAnnuelViewBean) viewBean;

        try {
            AFControleLppAnnuelProcess process = new AFControleLppAnnuelProcess();
            process.setSession((BSession) session);
            process.setAnneeDebut(vb.getAnneeDebut());
            process.setAnneeFin(vb.getAnneeFin());
            process.setTypeAdresse(vb.getTypeAdresse());
            process.setModeControle(vb.isModeControleSimulation());
            process.setEMailAddress(vb.getEmail());
            process.setFileName(vb.getFilename());
            process.setDateCreation(vb.getDateCreation());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
