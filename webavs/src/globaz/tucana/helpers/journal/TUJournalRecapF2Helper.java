package globaz.tucana.helpers.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.print.list.TUJournalRecapF2List;
import globaz.tucana.process.journal.TUJournalRecapF2Process;
import globaz.tucana.vb.journal.TUJournalRecapF2ViewBean;

/**
 * Definition du helper pour le domaine journal récap.
 * 
 * @author fgo date de création : 13.09.2007
 * @version : version 1.0
 */
public class TUJournalRecapF2Helper extends FWHelper {

    /**
     * Constructeur
     */
    public TUJournalRecapF2Helper() {
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
        TUJournalRecapF2ViewBean eViewBean = (TUJournalRecapF2ViewBean) viewBean;
        /*
         * action générer
         */
        if ("generer".equals(action.getActionPart())) {
            try {
                TUJournalRecapF2Process process = new TUJournalRecapF2Process();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setAnnee(eViewBean.getAnnee());
                process.setCsAgence(eViewBean.getCsAgence());
                BProcessLauncher.start(process);
                // process.executeProcess();
                if (process.isOnError()) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(process.getMessage());
                }
                if (viewBean instanceof TUJournalRecapF2ViewBean) {
                    ((TUJournalRecapF2ViewBean) viewBean).setJournal(process.getJournal());
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            return viewBean;
        } else if ("pdf".equals(action.getActionPart())) {
            try {
                TUJournalRecapF2List process = new TUJournalRecapF2List();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setAnnee(eViewBean.getAnnee());
                process.setCsAgence(eViewBean.getCsAgence());
                BProcessLauncher.start(process);
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
