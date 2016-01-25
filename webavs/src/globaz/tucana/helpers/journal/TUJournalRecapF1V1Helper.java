package globaz.tucana.helpers.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.print.list.TUJournalRecapF1V1List;
import globaz.tucana.process.journal.TUJournalRecapF1V1Process;
import globaz.tucana.vb.journal.TUJournalRecapF1V1ViewBean;

/**
 * Definition du helper pour le domaine journal récap.
 * 
 * @author fgo date de création : 3 juil. 06
 * @version : version 1.0
 */
public class TUJournalRecapF1V1Helper extends FWHelper {

    /**
     * Constructeur
     */
    public TUJournalRecapF1V1Helper() {
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
        TUJournalRecapF1V1ViewBean eViewBean = (TUJournalRecapF1V1ViewBean) viewBean;
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        // ACTION GENERER
        // -------------------------------------------------------------------------------------------------------------------------------------------------
        if ("generer".equals(action.getActionPart())) {
            try {
                TUJournalRecapF1V1Process process = new TUJournalRecapF1V1Process();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setAnnee(eViewBean.getAnnee());
                process.setMois(eViewBean.getMois());
                process.setCsAgence(eViewBean.getCsAgence());
                process.executeProcess();
                if (process.isOnError()) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(process.getMessage());
                }
                if (viewBean instanceof TUJournalRecapF1V1ViewBean) {
                    ((TUJournalRecapF1V1ViewBean) viewBean).setJournal(process.getJournal());
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            return viewBean;
        } else if ("pdf".equals(action.getActionPart())) {
            try {
                TUJournalRecapF1V1List process = new TUJournalRecapF1V1List();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setAnnee(eViewBean.getAnnee());
                process.setMois(eViewBean.getMois());
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

            /*
             * action exporter csv
             */
        } else {
            return super.execute(viewBean, action, session);
        }
    }

}
