package globaz.musca.helpers.interets;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.musca.db.interets.FAChangementJournalIMViewBean;
import globaz.musca.process.interets.FAChangementJournalIMProcess;

/**
 * @author MMO
 * @since 27 juillet 2010
 */
public class FAChangementJournalIMHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        FAChangementJournalIMViewBean vb = (FAChangementJournalIMViewBean) viewBean;

        try {
            FAChangementJournalIMProcess process = new FAChangementJournalIMProcess();
            process.setSession((BSession) session);
            process.setListIdIM(vb.getListIdIMATraiter());
            process.setIdJournal(vb.getIdJournal());
            process.setEMailAddress(vb.getEmail());
            vb.setISession(process.getSession());
            BProcessLauncher.start(process);

        } catch (Exception e) {
            JadeLogger.warn(this, e.toString());
        }
    }

}
