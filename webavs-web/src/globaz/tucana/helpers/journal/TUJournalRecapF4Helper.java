package globaz.tucana.helpers.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.tucana.print.list.TUJournalRecapF4List;
import globaz.tucana.process.journal.TUJournalRecapF4Process;
import globaz.tucana.vb.journal.TUJournalRecapF3ViewBean;
import globaz.tucana.vb.journal.TUJournalRecapF4ViewBean;

/**
 * Definition du helper pour le domaine journal récap.
 * 
 * @author fgo date de création : 08.10.2007
 * @version : version 1.0
 */
public class TUJournalRecapF4Helper extends FWHelper {

    /**
     * Constructeur
     */
    public TUJournalRecapF4Helper() {
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
        TUJournalRecapF4ViewBean eViewBean = (TUJournalRecapF4ViewBean) viewBean;
        /*
         * action générer
         */
        if ("generer".equals(action.getActionPart())) {
            try {
                TUJournalRecapF4Process process = new TUJournalRecapF4Process();
                process.setSession((BSession) session);
                process.setEMailAddress(eViewBean.getEMail());
                process.setAnnee(eViewBean.getAnnee());
                process.setMois(eViewBean.getMois());
                process.setCsAgence(eViewBean.getCsAgence());
                process.setCsRubriqueList(eViewBean.getCsRubriqueList());
                BProcessLauncher.start(process);
                // process.executeProcess();
                if (process.isOnError()) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(process.getMessage());
                }
                if (viewBean instanceof TUJournalRecapF3ViewBean) {
                    ((TUJournalRecapF3ViewBean) viewBean).setJournal(process.getJournal());
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
            return viewBean;
        } else if ("pdf".equals(action.getActionPart())) {
            try {
                TUJournalRecapF4List process = new TUJournalRecapF4List();
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
        } else {
            return super.execute(viewBean, action, session);
        }

    }
}
