package globaz.pavo.helpers.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.inscriptions.CIJournalComptabiliserViewBean;
import globaz.pavo.process.CIComptabiliserJournalProcess;

public class CIJournalComptabiliserHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        CIJournalComptabiliserViewBean vb = (CIJournalComptabiliserViewBean) viewBean;
        try {
            CIComptabiliserJournalProcess process = new CIComptabiliserJournalProcess(vb.getSession());
            process.setIdJournal(vb.getIdJournal());
            BProcessLauncher.start(process);
        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
