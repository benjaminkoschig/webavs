package globaz.aquila.helpers.process;

import globaz.aquila.db.process.COExecuterJournalViewBean;
import globaz.aquila.process.journal.COExecuterJournal;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;

/**
 * @author SEL <br>
 *         Date : 26 avr. 2010
 */
public class COExecuterJournalHelper extends FWHelper {
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session) {
        COExecuterJournalViewBean vjViewBean = (COExecuterJournalViewBean) viewBean;

        try {
            COExecuterJournal process = new COExecuterJournal();
            process.setSession((BSession) session);
            process.setEMailAddress(vjViewBean.getEMailAddress());
            process.setIdJournal(vjViewBean.getIdJournal());

            BProcessLauncher.start(process);
        } catch (Exception e) {
            vjViewBean.setMsgType(FWViewBeanInterface.ERROR);
            vjViewBean.setMessage(e.toString());
        }
    }
}
